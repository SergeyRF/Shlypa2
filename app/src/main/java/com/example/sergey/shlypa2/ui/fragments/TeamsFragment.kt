package com.example.sergey.shlypa2.ui.fragments


import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import kotlinx.android.synthetic.main.fragment_teams.*


/**
 * A simple [Fragment] subclass.
 */
class TeamsFragment : Fragment() {

    lateinit var viewModel: PlayersViewModel
    lateinit var adapterTeam: RvAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(activity!!).get(PlayersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeamsLiveData().observe(this, Observer { list -> setTeemRv(list) })
        viewModel.initTeams()

        btNext.setOnClickListener {
            viewModel.startSettings()
        }

        adapterTeam = RvAdapter()
        rvTeams.layoutManager = LinearLayoutManager(context)
        rvTeams.adapter = adapterTeam

        adapterTeam.listener = { team: Any ->
            dialog(team as Team)
        }

        fabAddTeam.setOnClickListener {
            viewModel.addTeam()
        }

        fabRemoveTeam.setOnClickListener {
            viewModel.reduceTeam()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.setTitleId(R.string.teem_actyvity)
    }

    private fun dialog(team: Team) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_edit_text)
        val etTeemD = dialog.findViewById<EditText>(R.id.etDialog)
        val btYesD = dialog.findViewById<Button>(R.id.btYesDialog)
        val btNoD = dialog.findViewById<Button>(R.id.btNoDialog)
        etTeemD.hint = team.name
        btYesD.setOnClickListener {
            if (etTeemD.text.isNotEmpty()) {
                team.name = etTeemD.text.toString()
                adapterTeam.notifyDataSetChanged()
                dialog.cancel()
            }
        }
        btNoD.setOnClickListener { dialog.cancel() }
        dialog.show()
    }

    private fun setTeemRv(teem: List<Team>?) {
        adapterTeam.setData(teem)
    }

}
