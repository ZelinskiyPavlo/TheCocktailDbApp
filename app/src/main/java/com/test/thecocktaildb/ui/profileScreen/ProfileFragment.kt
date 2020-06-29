package com.test.thecocktaildb.ui.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ProfileFragmentBinding
import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.dialog.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>(),
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>
{

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override val layoutId: Int = R.layout.profile_fragment

    override fun getViewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        open_test_fragment_btn.setOnClickListener {
            val testFragment = TestFragment.newInstance(4, "TEST_STRING")
            parentFragmentManager.beginTransaction()
                .replace(R.id.profile_fragment_container, testFragment)
                .addToBackStack(null)
                .commit()
        }

        profile_fragment_log_out_btn.setOnClickListener { view ->
            showLogOutBottomSheetDialog()
        }
    }

    private fun showLogOutBottomSheetDialog() {
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
                        requireContext().startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            }
        }
    }
}