package com.example.sergey.shlypa2.screens.players


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.extensions.runOnceEver
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayerSectionable
import com.example.sergey.shlypa2.screens.players.adapter.ItemTeam
import com.example.sergey.shlypa2.screens.players.adapter.ItemTeamSectionable
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_teams.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class TeamsFragment : androidx.fragment.app.Fragment() {

    val viewModel by sharedViewModel<PlayersViewModel>()
    private val teamAdapter = FlexibleAdapter(emptyList(), this)

    companion object {
        const val PLAYING_HINT = "playing_hint"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_teams, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTeamsLiveData().observeSafe(this) { teams -> onTeams(teams)}
        viewModel.initTeams()



        btNextWords.setOnClickListener {
            teamAdapter.currentItems.forEach {
                Timber.d("TESTING $it")
            }
            val reorderderedTeams = teamAdapter.currentItems.filterIsInstance<ItemTeamSectionable>()
                    .map { Team(it.team.name) }

            teamAdapter.currentItems.filterIsInstance<ItemPlayerSectionable>()
                    .forEach { item ->
                        reorderderedTeams.find { it.name == item.teamHeader?.team?.name}
                                ?.players?.add(item.player)
                    }

            reorderderedTeams.forEach {
                Timber.d("TESTING $it")
            }

            viewModel.startSettings()
        }

        rvTeams.layoutManager = PrecaheLayoutManager(context)
        rvTeams.adapter = teamAdapter
        teamAdapter.isLongPressDragEnabled = true

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

    private fun showTeamRenameDialog(itemTeam: ItemTeam) {
        val dialog = Dialog(requireContext())

        dialog.setContentView(R.layout.dialog_edit_text)
        val etTeemD = dialog.findViewById<EditText>(R.id.etDialog)
        val btYesD = dialog.findViewById<Button>(R.id.btYesDialog)
        val btNoD = dialog.findViewById<Button>(R.id.btNoDialog)
        etTeemD.hint = itemTeam.team.name

        btYesD.setOnClickListener {
            if (etTeemD.text.isNotEmpty()) {
                itemTeam.team.name = etTeemD.text.toString()
                teamAdapter.updateItem(itemTeam)
                etTeemD.requestFocus()
            }
            dialog.cancel()
        }
        btNoD.setOnClickListener { dialog.cancel() }

        etTeemD.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT && etTeemD.text.isNotEmpty()) {
                itemTeam.team.name = etTeemD.text.toString()
                teamAdapter.updateItem(itemTeam)
                dialog.cancel()
            }
            true

        }
        dialog.show()
    }

    private fun onTeams(teams: List<Team>) {
        /*teams.map {
            ItemTeam(it) { team -> showTeamRenameDialog(team) }
        }.apply {
            teamAdapter.updateDataSet(this)
        }*/

        val items = teams.map {
            val header = ItemTeamSectionable(it)
            it.players.forEach {
                header.addSubItem(ItemPlayerSectionable(header, it))
            }
            header
        }

        teamAdapter.updateDataSet(items)
        teamAdapter.expandAll()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        runGuide()
        return super.onOptionsItemSelected(item)
    }

    private fun runGuide() {
        val shaffleTeam = SimpleTarget.Builder(activity!!)
                .setPoint(floatingMenu.menuIconView)
                .setRadius(80f)
                .setTitle(getString(R.string.hint_team_shaffle))
                .setDescription(getString(R.string.hint_team_shaffle_button))

                .build()

        //Position for rename icon
        val x = Functions.getScreenWidth(activity!!) - 40
        val y = 40F + Functions.dpToPx(context!!, 72F)
        val renameGuide = SimpleTarget.Builder(activity!!)
                .setPoint(x.toFloat(), y)
                .setRadius(40f)
                .setTitle(getString(R.string.rename))
                .setDescription(getString(R.string.click_to_rename))
                .build()


        Spotlight.with(activity!!)
                .setOverlayColor(ContextCompat.getColor(activity!!, R.color.anotherBlack))
                .setDuration(100L)
                .setTargets(shaffleTeam, renameGuide)
                .setClosedOnTouchedOutside(true)
                .setAnimation(DecelerateInterpolator(2f))
                .start()

    }

    private fun globalListentrForSpotl() {
        requireContext().runOnceEver(PLAYING_HINT) {
            view?.onDrawn { runGuide() }
        }
    }

}
