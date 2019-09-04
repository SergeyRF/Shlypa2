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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.sergey.shlypa2.ImagesHelper
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayer
import com.example.sergey.shlypa2.screens.players.dialog.AvatarSelectDialogFragment
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.fragment_players.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class PlayersFragment : androidx.fragment.app.Fragment(),
        AvatarSelectDialogFragment.AvatarSelectDialogListener,
        FlexibleAdapter.OnItemSwipeListener,
        FlexibleAdapter.OnItemClickListener {


    val viewModel by sharedViewModel<PlayersViewModel>()
    private lateinit var playersAdapter: FlexibleAdapter<IFlexible<*>>

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
        playersAdapter = FlexibleAdapter(emptyList(), this)

        with(rvPlayers) {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
                reverseLayout = true
            }
            adapter = playersAdapter
        }
        // set swipe enabled only allowed after attaching to recycler view
        playersAdapter.isSwipeEnabled = true

        civPlayerAvatar.setOnClickListener {
            viewModel.onChangeAvatarClicked()
        }

        etName.setOnEditorActionListener { _, actionId, _ ->
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

        btCreateTeam.setOnClickListener {
            viewModel.onPlayersNextClicked()
        }

        fabPlayerRandom.setOnClickListener {
            viewModel.addRandomPlayer()
        }

        fabPlayerUser.setOnClickListener {
            viewModel.onAddFromSavedClicked()
        }
    }

    private fun initSubscriptions() {
        viewModel.playersLiveData.observeSafe(this, ::onPlayersChanged)
        viewModel.avatarLiveData.observeSafe(this, ::showAvatar)
    }

    override fun onSelectCustomAvatar() {
        startCropImageActivity()
    }

    private fun addPlayer() {
        viewModel.addNewPlayer(etName.text.toString())
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
            ItemPlayer(it)
        }.apply {
            playersAdapter.updateDataSet(this)
            rvPlayers.scrollToPosition(players.size - 1)
        }
    }

    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

    }

    override fun onItemSwipe(position: Int, direction: Int) {
        when (val item = playersAdapter.getItem(position)) {
            is ItemPlayer -> {
                viewModel.removePlayer(item.player)
            }
            else -> {
            }
        }
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        return when (val item = playersAdapter.getItem(position)) {
            is ItemPlayer -> {
                viewModel.onClickPlayer(item.player)
                true
            }
            else -> {
                false
            }
        }
    }

    private fun showAvatar(fileName: String) {
        Glide.with(this)
                .load(ImagesHelper.smallImagePathPlayer(fileName, requireContext()))
                .apply(avatarOptions)
                .into(civPlayerAvatar)
    }

    private fun runSpotlight() {

        val locations = intArrayOf(0, 0)
        floatingMenu.getLocationOnScreen(locations)

        val fabX = locations[0] + floatingMenu.width - 32.dpToPx
        val fabY = locations[1] + floatingMenu.height - 32.dpToPx

        val custom = SimpleTarget.Builder(activity!!)
                .setPoint(fabX.toFloat(), fabY.toFloat())
                .setRadius(70f)
                .setTitle(getString(R.string.hint_random_player))
                .setDescription(getString(R.string.hint_inject_random_player))
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {

                    override fun onStarted(target: SimpleTarget?) {
                    }

                    override fun onEnded(target: SimpleTarget?) {
                    }
                })
                .build()

        val injectName = SimpleTarget.Builder(requireActivity())
                .setRadius(80f)
                .setPoint(imageButton)
                .setTitle(getString(R.string.hint_inject_name))
                .setDescription(getString(R.string.hint_inject_name_button))
                .build()

        Spotlight.with(requireActivity())
                .setOverlayColor(ContextCompat.getColor(activity!!, R.color.anotherBlack))
                .setDuration(300L)
                .setTargets(injectName, custom)
                .setClosedOnTouchedOutside(true)
                .setAnimation(DecelerateInterpolator(2f))
                .start()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.hint_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_show_hint -> {
                runSpotlight()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSelectAvatar(iconString: String) {
        Timber.d("avatar select $iconString")
        showAvatar(iconString)
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
                //todo require refactoring
                Toast.makeText(requireContext(),
                        "Cancelling, required permissions are not granted",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
}
