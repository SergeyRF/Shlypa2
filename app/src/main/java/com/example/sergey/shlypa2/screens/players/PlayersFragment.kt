package com.example.sergey.shlypa2.screens.players


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.ui.dialogs.AvatarSelectDialog
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import com.squareup.picasso.Picasso
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import kotlinx.android.synthetic.main.fragment_players.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class PlayersFragment : androidx.fragment.app.Fragment() {

    lateinit var adapter: RvAdapter
    lateinit var viewModel: PlayersViewModel

    companion object {
        const val SHOW_SPOTLIGHT = "spotlight_show"
    }

    private var avatar: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        viewModel = ViewModelProviders.of(activity!!).get(PlayersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linLayout = androidx.recyclerview.widget.LinearLayoutManager(context)
        linLayout.stackFromEnd = true
        linLayout.reverseLayout = true

        rvPlayers.layoutManager = linLayout
        adapter = RvAdapter()
        rvPlayers.adapter = adapter



        viewModel.getPlayersLiveData().observe(this, Observer { list -> onPlayersChanged(list) })

        adapter.listener = { player: Any ->
            //todo player saves incorrect
            viewModel.reNamePlayer(player as Player)
        }

        adapter.listenerTwo = { player: Any ->

            viewModel.removePlayer(player as Player)

        }

        imageButton.setOnClickListener {
            addPlayer()
        }

        btGoNextPlayers.setOnClickListener {
            if (Game.getPlayers().size < 4) {
                Toast.makeText(context, R.string.not_enough_players, Toast.LENGTH_LONG).show()
            } else viewModel.startTeams()
        }

        btAddRandomPlayer.setOnClickListener {
            viewModel.addRandomPlayer()
        }

        //enter
        etName.setOnEditorActionListener { v, actionId, event ->
            if (this.isResumed/* strange error */ && actionId == EditorInfo.IME_ACTION_NEXT) {
                // обработка нажатия Enter
                try {
                    addPlayer()
                }catch (exeption:Exception){
                    Timber.e(exeption)
                }

                true
            } else true
        }


        viewModel.getAvatarLiveData().observe(this, Observer { avatar ->
            avatar?.let { showAvatar(it) }
        })


        civPlayerAvatar.setOnClickListener {
            val dialog = AvatarSelectDialog(requireContext(), viewModel.listOfAvatars)
            dialog.onSelect = dialogOnSelect
            dialog.show()
        }

    }

    val dialogOnSelect: (String) -> Unit = { fileName ->
        Timber.d("avatar select $fileName")
        showAvatar(fileName)
    }

    private fun addPlayer() {
        if (etName.text.isNotEmpty()) {
            val newPlayer = Player(etName.text.toString().trim())

            if (avatar != null) {
                newPlayer.avatar = avatar!!
            }

            viewModel.addPlayer(newPlayer).observe(this, Observer {
                if (it != null && it) {
                    etName.text.clear()
                } else {
                    Toast.makeText(context, R.string.name_not_unic, Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(context, R.string.player_name_empty, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.setTitleId(R.string.player_actyvity)
        globalListentrForSpotl()
    }

    private fun globalListentrForSpotl() {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        if (preference.getBoolean(SHOW_SPOTLIGHT, true)) {

            view!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    Timber.d("On global changed")
                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    spotl()
                    editor.putBoolean(SHOW_SPOTLIGHT, false).apply()
                }
            })
        }
    }


    private fun onPlayersChanged(players: List<Player>?) {
        adapter.setData(players)
        val position = players?.size ?: 0
        rvPlayers.scrollToPosition(position - 1)
    }

    private fun showAvatar(fileName: String) {
        avatar = fileName
        val fileLink = Functions.imageNameToUrl("player_avatars/small/$fileName")
        Picasso.get()
                .load(fileLink)
                .into(civPlayerAvatar)
    }

    fun spotl() {

        val custom = SimpleTarget.Builder(activity!!)
                .setPoint(btAddRandomPlayer)
                .setRadius(80f)
                .setTitle(getString(R.string.hint_random_player))
                .setDescription(getString(R.string.hint_inject_random_player))
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {

                    override fun onStarted(target: SimpleTarget?) {
                    }

                    override fun onEnded(target: SimpleTarget?) {
                    }
                })
                .build()
        val injectName = SimpleTarget.Builder(activity!!)
                .setRadius(80f)
                .setPoint(imageButton)
                .setTitle(getString(R.string.hint_inject_name))
                .setDescription(getString(R.string.hint_inject_name_button))
                .build()

        Spotlight.with(activity!!)
                .setOverlayColor(ContextCompat.getColor(activity!!, R.color.anotherBlack))
                .setDuration(300L)
                .setTargets(injectName, custom)
                .setClosedOnTouchedOutside(true)
                .setAnimation(DecelerateInterpolator(2f))
                .start()

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        spotl()
        return super.onOptionsItemSelected(item)
    }


}
