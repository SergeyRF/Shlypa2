package com.example.sergey.shlypa2.screens.players


import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.extensions.runOnceEver
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayerSectionable
import com.example.sergey.shlypa2.screens.players.adapter.ItemTeamSectionable
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.PrecaheLayoutManager
import com.example.sergey.shlypa2.utils.spotligth.Square
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.target.SimpleTarget
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_teams.*
import kotlinx.android.synthetic.main.holder_player_sectionable.view.*
import kotlinx.android.synthetic.main.holder_team_sectionable.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass.
 */
class TeamsFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

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
        viewModel.teamsLiveData.observeSafe(this) { teams -> onTeams(teams) }
        viewModel.initTeams()

        btNextWords.setOnClickListener {
            viewModel.saveTeamsAndStartSettings(getReorderedTeams())
        }

        with(rvTeams) {
            layoutManager = PrecaheLayoutManager(context)
            adapter = teamAdapter
        }

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
        runSpotlight()
    }

    private fun getReorderedTeams(): List<Team> {
        val newTeams = mutableListOf<Team>()
        var team: Team? = null

        teamAdapter.currentItems.forEach {
            if (it is ItemTeamSectionable) {
                team?.let { team -> newTeams.add(team) }
                team = Team(it.team.name)
            } else if (it is ItemPlayerSectionable) {
                team?.players?.add(it.player)
            }
        }

        team?.let { newTeams.add(it) }
        return newTeams
    }

    private fun onTeams(teams: List<Team>) {
        val items = mutableListOf<IFlexible<*>>()
        teams.forEach {
            items.add(ItemTeamSectionable(it))
            items.addAll(it.players.map { player -> ItemPlayerSectionable(player) })
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

    override fun onItemClick(view: View?, position: Int): Boolean {
        return when (val item = teamAdapter.getItem(position)) {
            is ItemTeamSectionable -> {
                viewModel.onTeamClicked(item.team)
                true
            }
            else -> false
        }
    }

    private fun runSpotlight() {
        requireContext().runOnceEver(PLAYING_HINT) {
            view?.onDrawn { runGuide() }
        }
    }

    private fun runGuide() {
        val shuffleGuide = SimpleTarget.Builder(activity!!)
                .setPoint(floatingMenu.menuIconView)
                .setShape(Circle(33f.dpToPx))
                .setDuration(700)
                .setTitle(getString(R.string.hint_team_shaffle))
                .setDescription(getString(R.string.hint_team_shaffle_button))
                .build()

        //Position for team name
        val teamNameView = rvTeams.getChildAt(0)?.tvTeamName
        val x = Functions.getScreenWidth(requireActivity()).toFloat() / 3
        val y = 40F + 72.dpToPx
        val renameGuide = SimpleTarget.Builder(requireActivity())
                .apply {
                    teamNameView?.let {
                        setPoint(it)
                    } ?: run {
                        setPoint(x, y)
                    }
                }
                .setShape(Square(
                        (teamNameView?.height ?: 35) + 16.dpToPx,
                        (teamNameView?.width ?: 35) + 16.dpToPx))
                .setTitle(getString(R.string.rename))
                .setDescription(getString(R.string.click_to_rename))
                .setDuration(700)
                .build()

        val playerAvatarView = rvTeams.getChildAt(1)?.ivPlayerAvatar
        val reorderGuide = SimpleTarget.Builder(requireActivity())
                .apply {
                    playerAvatarView?.let {
                        setPoint(it)
                    } ?: run {
                        setPoint(x, y + 50.dpToPx)
                    }
                }
                .setShape(Circle(30f.dpToPx))
                .setTitle(getString(R.string.team_reorder))
                .setDescription(getString(R.string.team_reorder_guide))
                .setDuration(700)
                .build()


        Spotlight.with(activity!!)
                .setOverlayColor(R.color.anotherBlack)
                .setDuration(700L)
                .setTargets(shuffleGuide, renameGuide, reorderGuide)
                .setClosedOnTouchedOutside(true)
                .setAnimation(DecelerateInterpolator(2f))
                .start()

    }
}
