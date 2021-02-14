package com.test.profile.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewSwitcher
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import coil.Coil
import coil.api.load
import coil.request.LoadRequest
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import com.test.presentation.extension.*
import com.test.presentation.extension.BitmapHelper.Companion.convertMbToBinaryBytes
import com.test.presentation.extension.BitmapHelper.Companion.getBitmap
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.*
import com.test.profile.R
import com.test.profile.databinding.FragmentProfileBinding
import com.test.profile.factory.ProfileViewModelFactory
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1932
        const val REQUEST_CODE_IMAGE_CHOOSER_PERMISSION = 1933

        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_profile

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    override val viewModel: ProfileViewModel by viewModels {
        SavedStateViewModelFactory(profileViewModelFactory, this)
    }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val isReadExternalStoragePermissionGranted
        get() = requireActivity().isAllPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)

    private val viewSwitcher: ViewSwitcher by lazy { viewDataBinding.profileFragmentViewSwitcher }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        configureToolbar()
        configureObserver()
        setWhiteSpaceFilter()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    private fun configureToolbar() {
//        viewDataBinding.profileFragmentToolbar.backButton.setOnClickListener {
//        }

        viewDataBinding.profileFragmentToolbar.primaryOption.setOnClickListener {
            showLogOutBottomSheetDialog()
        }
    }

    private fun configureObserver() {
//        viewModel.logOutUserEventLiveData.observe(viewLifecycleOwner, EventObserver {
//            val intent = Intent(requireContext(), AuthActivity::class.java)
//            requireContext().startActivity(intent)
//            activity?.finish()
//        })

        viewModel.userDataChangedLiveData.observeNotNull(viewLifecycleOwner, {
            firebaseAnalytics.logUserNameChanged(viewModel.userFullNameLiveData.value)

            viewSwitcher.showNext()
        })

        viewModel.userAvatarLiveData.observeNotNull(viewLifecycleOwner) { avatar ->
            viewDataBinding.profileFragmentAvatar.load(avatar) {
                crossfade(true)
                lifecycle(this@ProfileFragment)
                placeholder(R.drawable.placeholder_avatar)
                error(R.drawable.placeholder_error_avatar)
                transformations(listOf(CircleCropTransformation()))
            }
            val request = LoadRequest.Builder(requireContext())
                .crossfade(true)
                .lifecycle(this)
                .data(avatar)
                .target { drawable ->
                    viewDataBinding.profileAppBarLayout.background = drawable
                }
                .placeholder(R.drawable.placeholder_background)
                .error(R.drawable.placeholder_error_background)
                .transformations(BlurTransformation(requireContext()))
                .build()
            Coil.imageLoader(requireContext()).execute(request)
        }
    }

    private fun setWhiteSpaceFilter() {
        val whiteSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            source.filterNot { char -> char.isWhitespace() }
        }
        with(viewDataBinding) {
            profileFragmentChangeNameEt.filters =
                profileFragmentChangeNameEt.filters + whiteSpaceFilter
            profileFragmentChangeLastNameEt.filters =
                profileFragmentChangeLastNameEt.filters + whiteSpaceFilter
            profileFragmentChangeEmailEt.filters =
                profileFragmentChangeEmailEt.filters + whiteSpaceFilter
        }
    }

    fun toggleUserChange() {
        with(viewModel) {
            viewDataBinding.profileFragmentChangeNameEt.setText(userNameLiveData.value)
            viewDataBinding.profileFragmentChangeLastNameEt.setText(userLastNameLiveData.value)
            viewDataBinding.profileFragmentChangeEmailEt.setText(userEmailLiveData.value)
        }

        viewSwitcher.showNext()
    }

    fun saveUserChange() {
        if (viewModel.isInputDataInvalid()) {
            showInvalidInputDialog()
            return
        }
        if (viewModel.isUserDataChanged()) {
            viewModel.updateUser()
        } else {
            viewSwitcher.showPrevious()
        }
        viewSwitcher.requestFocus()
    }

    fun changeAvatar() {
        if (viewSwitcher.nextView.id == R.id.profile_fragment_change_user_info_layout)
            return
        checkOrRequestReadExternalStoragePermission(onGranted = ::openGalleryImageChooser)
    }

    private fun showLogOutBottomSheetDialog() {
        RegularBottomSheetDialogFragment.newInstance {
            titleTextResId = R.string.dialog_log_out_title
            descriptionTextResId = R.string.dialog_log_out_description
            leftButtonTextResId = R.string.all_cancel
            rightButtonTextResId = R.string.all_exit
        }.show(childFragmentManager, "LogOutDialog")
    }

    private fun showInvalidInputDialog() {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.invalid_new_user_data_dialog_title
            descriptionTextResId = R.string.invalid_new_user_data_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(childFragmentManager, "InvalidDataDialog")
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            RegularDialogType -> {
                when (buttonType) {
                    LeftDialogButton -> dialog.dismiss()
                    RightDialogButton -> {
                        viewModel.logOutUser()
                    }
                }
            }
        }
    }

    private fun openGalleryImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CODE_PICK_IMAGE
        )
    }

    private fun checkOrRequestReadExternalStoragePermission(onGranted: () -> Unit) {
        if (!isReadExternalStoragePermissionGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_IMAGE_CHOOSER_PERMISSION
            )
        } else onGranted()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        fun logAvatarChangeEvent(oldAvatarUrl: String) {
            viewModel.userAvatarLiveData.observeUntil(
                viewLifecycleOwner,
                { newAvatarUrl -> oldAvatarUrl != newAvatarUrl },
                { newAvatarUrl ->
                    firebaseAnalytics.logUserAvatarChanged(
                        newAvatarUrl,
                        viewModel.userFullNameLiveData.value
                    )
                }
            )
        }

        when {
            resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_IMAGE && data != null -> {
                val uri = data.data ?: return
//                val imageFile = File(uri.pathCompat!!)

//                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                val bitmap = getBitmap(requireContext(), uri)!!
                    .also { Timber.i("LOG bitmap size before = ${it.byteCount}") }
                    .scaleToSize(convertMbToBinaryBytes(1))
                    .also {
                        Timber.i(
                            "LOG bitmap size after = ${it.byteCount}, " +
                                    "max= ${convertMbToBinaryBytes(1)}, " +
                                    "dif=${convertMbToBinaryBytes(1) - it.byteCount}"
                        )
                    }
                /*We can access getExternalFileDir() without asking any storage permission.*/
                val imageFile = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    System.currentTimeMillis().toString() + "_temp.png"
                )

                // we need to write to this file
                bitmap.convertBitmapToFile(imageFile)

                logAvatarChangeEvent(viewModel.userAvatarLiveData.value ?: "")
                viewModel.uploadAvatar(imageFile) { fraction ->
                    Timber.i("LOG PROGRESS = fraction=$fraction, percent=${fraction * 100.0F}%")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_IMAGE_CHOOSER_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    openGalleryImageChooser()
                } else {
                    showNoPermissionGrantedDialog()
                }
            }
        }
    }

    private fun showNoPermissionGrantedDialog() {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.no_permission_dialog_title
            descriptionTextResId = R.string.no_permission_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(parentFragmentManager, "ShowPermissionDialog")
    }
}