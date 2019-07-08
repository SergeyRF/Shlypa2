package com.example.sergey.shlypa2.screens.game


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.getLargeImage
import com.example.sergey.shlypa2.utils.Functions
import de.hdodenhof.circleimageview.CircleImageView


/**
 * A simple [Fragment] subclass.
 */
class TurnStartFragment : androidx.fragment.app.Fragment() {

    lateinit var viewModel: RoundViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(activity!!).get(RoundViewModel::class.java)

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_turn_start, container, false)

        val playerTv: TextView = root.findViewById(R.id.tvTurnPlayerName)
        val startButton: Button = root.findViewById(R.id.btTurnStart)
        val playerAvatar: CircleImageView = root.findViewById(R.id.civPlayerAvatar)
        val teamName: TextView = root.findViewById(R.id.tv_TurnTeamName)

        viewModel.roundLiveData.observe(this, Observer { round ->
            round?.let {
                teamName.text = it.currentTeam.name
                playerTv.text = it.getPlayer().name
                Glide.with(this)
                        .load(it.getPlayer().getLargeImage(requireContext()))
                        .into(playerAvatar)
            }
        })
        startButton.setOnClickListener { viewModel.startTurn() }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_show_hint) {

            viewModel.loadHintTeam()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
