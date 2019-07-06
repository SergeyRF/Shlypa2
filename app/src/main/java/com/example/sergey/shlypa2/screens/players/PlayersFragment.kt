package com.example.sergey.shlypa2.screens.players



import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.beans.Player
import com.example.sergey.shlypa2.extensions.dpToPx
import com.example.sergey.shlypa2.extensions.observeSafe
import com.example.sergey.shlypa2.extensions.onDrawn
import com.example.sergey.shlypa2.game.Game
import com.example.sergey.shlypa2.screens.players.adapter.ItemPlayer
import com.example.sergey.shlypa2.ui.dialogs.AvatarSelectDialog
import com.example.sergey.shlypa2.ui.dialogs.CustomAvatar
import com.example.sergey.shlypa2.utils.Functions
import com.example.sergey.shlypa2.utils.glide.CircleBorderTransform
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.SimpleTarget
import com.takusemba.spotlight.Spotlight
import com.theartofdev.edmodo.cropper.CropImage
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
        const val GALLERY_REQUEST = 10
        private const val REQUEST_CODE_PERMISSION_READ_IMAGE = 120
        private const val REQUEST_IMAGE_CAPTURE = 110
        private const val REQUEST_GET_IMAGE_CAPTURE = 1100
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
                .load(Player.smallImagePath(fileName))
                .apply(avatarOptions)
                .into(civPlayerAvatar)
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

    private val getCustomAvatar: (CustomAvatar) -> Unit = { custom ->

        CropImage.activity()
                .start(requireContext(), this)
        /* when(custom){
             CustomAvatar.IMAGE->getImage()
             CustomAvatar.PHOTO->getPhoto()
         }*/
    }

    private fun startCropImageActivity(){
        CropImage.activity()
                .start(requireContext(), this)
    }
    private fun startCropImageActivity(imageUri:Uri){
        CropImage.activity(imageUri)
                .start(requireContext(),this)
    }
    private var mCropImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                val imageUri = CropImage.getPickImageResultUri(requireContext(), data)
                val result =  CropImage.getActivityResult(data).uri

                Glide.with(this)
                        .load(result)
                        .apply(avatarOptions)
                        .into(civPlayerAvatar)

                if (Build.VERSION.SDK_INT >= 23) {
                    if (CropImage.isReadExternalStoragePermissionsRequired(requireContext(), imageUri)) {
                        mCropImageUri = imageUri
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                    }else{
                        startCropImageActivity()
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null
                    && grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(mCropImageUri!!)
            }else{
                Toast.makeText(requireContext(),
                        "Cancelling, required permissions are not granted",
                        Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
/*
    private fun getImage() {
        val permissionStatus = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            loadImage()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION_READ_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_READ_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImage()
            } else {

            }
        }
        if (requestCode == REQUEST_GET_IMAGE_CAPTURE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
        return
    }

    private fun loadImage() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(photoPickerIntent,
                GALLERY_REQUEST)
    }

    private fun getPhoto() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_GET_IMAGE_CAPTURE)
        }
    }*/

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         // super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK && data != null) {
             when (requestCode) {
                 GALLERY_REQUEST -> {
                    *//* CropImage.activity(data.data)
                            .start(requireContext(), this)*//*
                }


                REQUEST_IMAGE_CAPTURE -> {
                    *//*  val imageBitmap = data.extras.get("data") as Bitmap
                      ivWorkoutImage.setImageBitmap(imageBitmap)*//*
                    //todo do it
                }

               *//* CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val imageResultUri = CropImage.getActivityResult(data).uri
                    val path = imageResultUri.path
                    val options = RequestOptions()
                            .centerCrop()
                    Glide.with(requireContext())
                            .load(File(path))
                            .apply(options)
                            .into(ivWorkoutImage)
                    ivWorkoutImage.show()
                    viewModelEdit.setImage(File(path))
                }*//*
            }
        }
        *//*if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //todo i don't know
            Timber.d("Exception error = result.getError();")
        }*//*
    }*/
}
