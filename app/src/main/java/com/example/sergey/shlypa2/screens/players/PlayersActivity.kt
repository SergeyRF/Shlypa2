package com.example.sergey.shlypa2.screens.players

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.RvAdapter
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.beans.Team
import com.example.sergey.shlypa2.extensions.*
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
        private const val DIALOG_RENAME_TAG = "teams_rename_dialog_tag"
        private const val DIALOG_AVATAR_TAG = "dialog_avatar_tag"
        private const val DIALOG_SELECT_PLAYER_TAG = "dialog_select_player"
        private const val REQUEST_WRITE_STORAGE_PERMISSION = 1034
        private const val REQUEST_CAMERA_PERMISSION = 1035
        private const val REQUEST_GALLERY = 1036
        private const val REQUEST_CAMERA = 1037

        fun getIntent(context: Context) = Intent(context, PlayersActivity::class.java)
    }

    lateinit var adapter: RvAdapter

    val viewModel by viewModel<PlayersViewModel>()

    private var cameraUri: Uri? = null

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
                .setCustomAnimations(
                        android.R.animator.fade_in, android.R.animator.fade_out,
                        android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
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
                viewModel.renamePlayer(newName, id)
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

    override fun onSelectAddPhoto() {
        if (isPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            cameraUri = photoFromCamera(REQUEST_CAMERA)
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
            )
        }
    }

    override fun onSelectAddFromGallery() {
        if (isPermissionsGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            openGalleryIntent(REQUEST_GALLERY)
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE_PERMISSION)
        }
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

    private fun setAvatarCustom(imageUri: Uri) {
        viewModel.addImage(imageUri)
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(150, 150)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
    }

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
                REQUEST_GALLERY -> {
                    data.data?.let {
                        startCropImageActivity(it)
                    }
                }
                REQUEST_CAMERA -> {
                    if (resultCode == Activity.RESULT_OK && cameraUri != null) {
                        cameraUri?.let {
                            startCropImageActivity(it)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_WRITE_STORAGE_PERMISSION -> onSelectAddFromGallery()
                REQUEST_CAMERA_PERMISSION -> onSelectAddPhoto()
            }
        }
    }
}
