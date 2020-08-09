package com.test.thecocktaildb.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentProfileBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.SharedMainViewModel
import com.test.thecocktaildb.ui.dialog.*
import com.test.thecocktaildb.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.util.EXTRA_KEY_SELECTED_LANGUAGE
import com.test.thecocktaildb.util.LANG_SHARED_PREFS
import com.test.thecocktaildb.util.LanguageType
import com.test.thecocktaildb.util.getSelectedLanguageIndex

class ProfileFragment : Injectable,
    BaseFragment<FragmentProfileBinding>(),
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private val sharedViewModel: SharedMainViewModel by activityViewModels()

    override val layoutId: Int = R.layout.fragment_profile

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
            titleText = getString(R.string.dialog_log_out_title)
            descriptionText = getString(R.string.dialog_log_out_description)
            leftButtonText = getString(R.string.dialog_log_out_cancel)
            rightButtonText = getString(R.string.dialog_log_out_accept)
        }.show(childFragmentManager, "LogOutFragment")
    }

    fun showChangeLanguageDialog() {
        val localeIndex = getSelectedLanguageIndex(requireActivity())
        LanguageListBottomSheetDialogFragment.newInstance(localeIndex)
            .show(childFragmentManager, "LanguageDialog")
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
            LanguageDialogType -> {
                when (buttonType) {
                    ItemListDialogButton -> {
                        val chosenLanguage = data as? LanguageType

                        val sharedPref = activity?.getSharedPreferences(
                            LANG_SHARED_PREFS, Context.MODE_PRIVATE
                        ) ?: return
                        with(sharedPref.edit()) {
                            putInt(EXTRA_KEY_SELECTED_LANGUAGE, chosenLanguage!!.ordinal)
                            apply()
                        }
                        activity?.recreate()
                    }
                }
            }
        }
    }
}