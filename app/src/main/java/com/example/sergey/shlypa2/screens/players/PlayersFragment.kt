package com.example.sergey.shlypa2.screens.players


import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayer
import com.example.sergey.shlypa2.ui.dialogs.AvatarSelectDialog
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import com.squareup.picasso.Picasso
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_players.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class PlayersFragment : androidx.fragment.app.Fragment() {

    val viewModel by sharedViewModel<PlayersViewModel>()
    private val playersAdapter = FlexibleAdapter(emptyList(), this)

    companion object {
        const val SHOW_SPOTLIGHT = "spotlight_show"
    }

    private var avatar: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSubscriptions()
    }

    private fun initViews() {
        rvPlayers.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
            reverseLayout = true
        }
        rvPlayers.adapter = playersAdapter

        civPlayerAvatar.setOnClickListener {
            val dialog = AvatarSelectDialog(requireContext(), viewModel.listOfAvatars)
            dialog.onSelect = dialogOnSelect
            dialog.show()
        }

        //enter
        etName.setOnEditorActionListener { v, actionId, event ->
            if (this.isResumed/* strange error */ && actionId == EditorInfo.IME_ACTION_NEXT) {
                // обработка нажатия Enter
                runCatching {
                    addPlayer()
                }.onFailure {
                    Timber.e(it)
                }

                true
            } else true
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
    }

    private fun initSubscriptions() {
        viewModel.getPlayersLiveData().observeSafe(this) { list ->
            onPlayersChanged(list)
        }

        viewModel.getAvatarLiveData().observe(this, Observer { avatar ->
            avatar?.let { showAvatar(it) }
        })
    }

    val dialogOnSelect: (String) -> Unit = { fileName ->
        Timber.d("avatar select $fileName")
        showAvatar(fileName)
    }

    private fun addPlayer() {
        viewModel.addPlayer(etName.text.toString())
        etName.text.clear()
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
            view?.onDrawn {
                runSpotlight()
                editor.putBoolean(SHOW_SPOTLIGHT, false).apply()
            }
        }
    }

    private fun onPlayersChanged(players: List<Player>) {
        players.map {
            ItemPlayer(it, renameListener = {
                viewModel.reNamePlayer(it)
            }, removeListener = {
                viewModel.removePlayer(it)
            })
        }.apply {
            playersAdapter.updateDataSet(this)
            rvPlayers.scrollToPosition(players.size - 1)
        }
    }

    private fun showAvatar(fileName: String) {
        avatar = fileName
        val fileLink = Functions.imageNameToUrl("player_avatars/small/$fileName")
        Picasso.get()
                .load(fileLink)
                .into(civPlayerAvatar)
    }

    private fun runSpotlight() {

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
        inflater?.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.item_show_hint -> {
                runSpotlight()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
