package com.example.sergey.shlypa2.ui.fragments


import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.preference.PreferenceManager
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import com.example.sergey.shlypa2.viewModel.PlayersViewModel
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import kotlinx.android.synthetic.main.fragment_teams.*


/**
 * A simple [Fragment] subclass.
 */
class TeamsFragment : Fragment() {

    lateinit var viewModel: PlayersViewModel
    lateinit var adapterTeam: RvAdapter

    companion object {
        const val PLAYING_HINT = "playing_hint"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(activity!!).get(PlayersViewModel::class.java)

        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeamsLiveData().observe(this, Observer { list -> setTeemRv(list) })
        viewModel.initTeams()

        btNextWords.setOnClickListener {
            viewModel.startSettings()
        }

        adapterTeam = RvAdapter()
        val layoutManager = PrecaheLayoutManager(context)
        rvTeams.layoutManager = layoutManager

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

        fabShuffleTeams.setOnClickListener {
            viewModel.shuffleTeams()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.setTitleId(R.string.teem_actyvity)
        //show hint if first game
        globalListentrForSpotl()
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
            }
            dialog.cancel()

        }
        btNoD.setOnClickListener { dialog.cancel() }

        etTeemD.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && etTeemD.text.isNotEmpty()) {
                team.name = etTeemD.text.toString()
                adapterTeam.notifyDataSetChanged()
                dialog.cancel()
            }
            true

        }
        dialog.show()
    }

    private fun setTeemRv(teem: List<Team>?) {
        adapterTeam.setData(teem)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        spotl()
        return super.onOptionsItemSelected(item)
    }

    fun spotl() {

        val shaffleTeam = SimpleTarget.Builder(activity!!)
                .setPoint(floatingMenu.menuIconView)
                .setRadius(80f)
                .setTitle(getString(R.string.hint_team_shaffle))
                .setDescription(getString(R.string.hint_team_shaffle_button))

                .build()


        Spotlight.with(activity!!)
                .setOverlayColor(ContextCompat.getColor(activity!!, R.color.anotherBlack))
                .setDuration(100L)
                .setTargets(shaffleTeam)
                .setClosedOnTouchedOutside(true)
                .setAnimation(DecelerateInterpolator(2f))

                .start()

    }

    private fun globalListentrForSpotl() {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        if (preference.getBoolean(TeamsFragment.PLAYING_HINT, true)) {

            view!!.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    spotl()
                    editor.putBoolean(TeamsFragment.PLAYING_HINT, false).apply()
                }
            })
        }
    }

}
