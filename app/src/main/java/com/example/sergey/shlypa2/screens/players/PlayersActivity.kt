package com.example.sergey.shlypa2.screens.players

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.extensions.dismissDialogFragment
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.setThemeApi21
import com.example.sergey.shlypa2.screens.game_settings.GameSettingsActivity
import com.example.sergey.shlypa2.screens.players.PlayersViewModel.Command
import com.example.sergey.shlypa2.screens.players.dialog.AvatarSelectDialogFragment
import com.example.sergey.shlypa2.screens.players.dialog.RenameDialogFragment
import com.example.sergey.shlypa2.screens.players.dialog.SelectPlayerDialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PlayersActivity : AppCompatActivity(), RenameDialogFragment.RenameDialogListener,
        AvatarSelectDialogFragment.AvatarSelectDialogListener {

    companion object {
        const val DIALOG_RENAME_TAG = "teams_rename_dialog_tag"
        const val DIALOG_AVATAR_TAG = "dialog_avatar_tag"
        const val DIALOG_SELECT_PLAYER_TAG = "dialog_select_player"

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

    private fun onCommand(command: Command) {
        when (command) {
            Command.StartSettings -> startSettings()
            Command.StartTeams -> startTeamsFragment()
            Command.ShowSelectPlayerDialog -> showPlayerSelectDialog()
            Command.ShowSelectAvatarDialog -> showAvatarDialog()
            is Command.ShowTeamRenameDialog -> showTeamRenameDialog(command.team)
            is Command.ShowPlayerRenameDialog -> showPlayerRenameDialog(command.player)
        }
    }

    private fun showPlayerSelectDialog() {
        dismissDialogFragment(DIALOG_SELECT_PLAYER_TAG)
        SelectPlayerDialogFragment().show(supportFragmentManager, DIALOG_SELECT_PLAYER_TAG)
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

    private fun showAvatarDialog() {
        dismissDialogFragment(DIALOG_AVATAR_TAG)

        AvatarSelectDialogFragment.newInstance(viewModel.listOfAvatars)
                .show(supportFragmentManager, DIALOG_AVATAR_TAG)
    }

    private fun showTeamRenameDialog(team: Team) {
        dismissDialogFragment(DIALOG_RENAME_TAG)

        RenameDialogFragment.newInstance(
                team.name,
                getString(R.string.team_rename),
                team.id.toLong(),
                RenameDialogFragment.EntityType.TEAM
        ).show(supportFragmentManager, DIALOG_RENAME_TAG)
    }

    private fun showPlayerRenameDialog(player: Player) {
        (supportFragmentManager
                .findFragmentByTag(DIALOG_RENAME_TAG) as? RenameDialogFragment)
                ?.dismissAllowingStateLoss()

        RenameDialogFragment.newInstance(
                player.name,
                getString(R.string.player_rename),
                player.id,
                RenameDialogFragment.EntityType.PLAYER
        ).show(supportFragmentManager, DIALOG_RENAME_TAG)
    }

    override fun onRenamed(newName: String, oldName: String, id: Long, type: RenameDialogFragment.EntityType) {
        when (type) {
            RenameDialogFragment.EntityType.TEAM -> {
                viewModel.renameTeam(newName, oldName)
            }
            RenameDialogFragment.EntityType.PLAYER -> {
                //todo don't create new player here
                viewModel.renamePlayer(Player(newName, id = id))
            }
        }


    }

    private fun startSettings() {
        startActivity(Intent(this, GameSettingsActivity::class.java))
    }

    override fun onSelectAvatar(iconString: String) {
        Timber.d("avatar select $iconString")
        viewModel.addImage(iconString)
    }

    override fun onSelectCustomAvatar() {
        startCropImageActivity()
    }


    private fun initToolbar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) is PlayersFragment) {
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

    //Load custom avatar

    private fun setAvatarCustom(imageUri: Uri) {
        viewModel.addImage(imageUri)
    }

    private fun startCropImageActivity(imageUri: Uri? = null) {
        val cropImage: CropImage.ActivityBuilder = if (imageUri != null) {
            CropImage.activity(imageUri)
        } else {
            CropImage.activity()
        }
        cropImage
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(150, 150)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)


    }

    private var mCropImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        val resultUri = result.uri
                        setAvatarCustom(resultUri)
                    } else {
                        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Timber.e(result.error)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(mCropImageUri)
            } else {
                //todo require refactoring
                Toast.makeText(this,
                        "Cancelling, required permissions are not granted",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
}
