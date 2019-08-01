package com.example.sergey.shlypa2.screens.players

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsActivity
import com.example.sergey.shlypa2.screens.players.dialog.RenameDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayersActivity : AppCompatActivity(), RenameDialogFragment.RenameDialogListener {

    companion object {
        const val DIALOG_RENAME_TAG = "teams_rename_dialog_tag"

        fun getIntent(context: Context) = Intent(context, PlayersActivity::class.java)
    }

    lateinit var adapter: RvAdapter

    val viewModel by viewModel<PlayersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeApi21()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

        initToolbar()

        viewModel.commandLiveData.observe(this, Observer { command ->
            if (command != null) onCommand(command)
        })

        viewModel.titleLiveData.observe(this, Observer { titleId ->
            if (titleId != null) setTitle(titleId)
        })

        viewModel.toastResLD.observeSafe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.teamRenameLiveData.observeSafe(this) {
            showTeamRenameDialog(it)
        }

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            startPlayersFragment()
        }
    }

    private fun onCommand(command: PlayersViewModel.Command) {
        when (command) {
            PlayersViewModel.Command.START_SETTINGS -> startSettings()
            PlayersViewModel.Command.START_TEAMS -> startTeamsFragment()
        }
    }

    private fun startPlayersFragment() {
        val fragment = PlayersFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    private fun startTeamsFragment() {
        val fragment = TeamsFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
    }

    private fun showTeamRenameDialog(team: Team) {
        (supportFragmentManager
                .findFragmentByTag(DIALOG_RENAME_TAG) as? RenameDialogFragment)
                ?.dismissAllowingStateLoss()

        RenameDialogFragment.newInstance(
                team.name,
                getString(R.string.team_rename),
                team.id.toLong(),
                RenameDialogFragment.EntityType.TEAM
        ).show(supportFragmentManager, DIALOG_RENAME_TAG)
    }

    override fun onRenamed(newName: String, oldName: String, id: Long, type: RenameDialogFragment.EntityType) {
        viewModel.renameTeam(newName, oldName)
    }

    private fun startSettings() {
        startActivity(Intent(this, GameSettingsActivity::class.java))
    }

    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.container) is PlayersFragment) {
            viewModel.onBackPressed()
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
