package com.test.thecocktaildb.ui.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ProfileFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.SharedMainViewModel
import com.test.thecocktaildb.ui.dialog.*

class ProfileFragment : Injectable,
    BaseFragment<ProfileFragmentBinding, ProfileViewModel>(),
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private val sharedViewModel: SharedMainViewModel by activityViewModels()

    override val layoutId: Int = R.layout.profile_fragment

    override fun getViewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = sharedViewModel
        viewDataBinding.fragment = this
    }

    fun openTestFragment() {
        val testFragment = TestFragment.newInstance(4, "TEST_STRING")
        parentFragmentManager.beginTransaction()
            .replace(R.id.profile_fragment_container, testFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showLogOutBottomSheetDialog() {
        RegularBottomSheetDialogFragment.newInstance {
            titleText = "Log Out"
            descriptionText = "Do you really want to exit?"
            leftButtonText = "Cancel"
            rightButtonText = "Exit"
        }.show(childFragmentManager, "LogOutFragment")
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
                        requireContext()
                            .startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            }
        }
    }
}