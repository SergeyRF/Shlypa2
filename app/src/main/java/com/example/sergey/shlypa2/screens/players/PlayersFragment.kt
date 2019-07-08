package com.example.sergey.shlypa2.screens.players


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.game.AvatarType
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayer
import com.example.sergey.shlypa2.ui.dialogs.AvatarSelectDialog
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.ActivityBuilder
import com.theartofdev.edmodo.cropper.CropImageView
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

    private val borderColor by lazy { Functions.getThemedBgColor(requireContext()) }
    private val avatarOptions by lazy(LazyThreadSafetyMode.NONE) {
        RequestOptions()
                .transforms(CircleCrop(), CircleBorderTransform(borderColor, 1.dpToPx))
    }

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
            dialog.onSelectCustom = getCustomAvatar
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

        viewModel.avatarLiveData.observeSafe(this) {
            showAvatar(it)
        }
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
        Glide.with(this)
                .load(ImagesHelper.smallImagePathPlayer(fileName, requireContext()))
                .apply(avatarOptions)
                .into(civPlayerAvatar)
        viewModel.addImage(fileName)
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
        return when (item?.itemId) {
            R.id.item_show_hint -> {
                runSpotlight()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Load custom avatar

    private val getCustomAvatar: () -> Unit = {
        startCropImageActivity()
    }

    private fun setAvatarCustom(imageUri: Uri) {
        Glide.with(this)
                .load(imageUri)
                .apply(avatarOptions)
                .into(civPlayerAvatar)
        //viewModel.addImage(imageUri.toString(), AvatarType.USER)
        viewModel.addImage(imageUri)
    }

    private fun startCropImageActivity(imageUri: Uri? = null) {
        val cropImage: ActivityBuilder = if (imageUri != null) {
            CropImage.activity(imageUri)
        } else {
            CropImage.activity()
        }
        cropImage
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(150, 150)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(requireContext(), this)


    }

    private var mCropImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
                Toast.makeText(requireContext(),
                        "Cancelling, required permissions are not granted",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }

}
