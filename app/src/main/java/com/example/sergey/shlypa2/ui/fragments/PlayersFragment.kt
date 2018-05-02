package com.example.sergey.shlypa2.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
import kotlinx.android.synthetic.main.fragment_players.*
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class PlayersFragment : Fragment() {

    lateinit var adapter: RvAdapter
    lateinit var viewModel: PlayersViewModel

    private var avatar: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        viewModel = ViewModelProviders.of(activity!!).get(PlayersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linLayout = LinearLayoutManager(context)
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

        adapter.listenerTwo = { player:Any->

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
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // обработка нажатия Enter
                addPlayer()
                true
            } else true
        }


        viewModel.getAvatarLiveData().observe(this, Observer { avatar ->
            avatar?.let { showAvatar(it) }
        })


        civPlayerAvatar.setOnClickListener {
            val dialog = AvatarSelectDialog(context!!, viewModel.listOfAvatars)
            dialog.onSelect = dialogOnSelect
            dialog.show()
        }
    }

    val dialogOnSelect : (String) -> Unit = {fileName ->
        Timber.d("avatar select $fileName")
        showAvatar(fileName)
    }

    private fun addPlayer() {
        if (etName.text.isNotEmpty()) {
            val newPlayer = Player(etName.text.toString().trim())

            if (avatar != null) {
                newPlayer.avatar = avatar!!
            }

            if (viewModel.addPlayer(newPlayer)) {
                etName.text.clear()
            } else {
                Toast.makeText(context, R.string.name_not_unic, Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, R.string.player_name_empty, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.setTitleId(R.string.player_actyvity)
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
}
