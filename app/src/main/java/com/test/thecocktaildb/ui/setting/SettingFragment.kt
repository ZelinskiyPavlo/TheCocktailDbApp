package com.test.thecocktaildb.ui.setting

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentSettingBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.SharedMainViewModel
import com.test.thecocktaildb.ui.cocktail.callback.BatteryStateCallback
import com.test.thecocktaildb.ui.dialog.*
import com.test.thecocktaildb.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.ui.setting.profile.ProfileFragment
import com.test.thecocktaildb.ui.setting.test.TestFragment
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SettingViewModelFactory
import com.test.thecocktaildb.util.batterystate.BatteryStateHolder
import com.test.thecocktaildb.util.locale.LanguageType
import com.test.thecocktaildb.util.receiver.BatteryStateReceiver
import javax.inject.Inject

class SettingFragment : Injectable, BatteryStateCallback,
    BaseFragment<FragmentSettingBinding>(),
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    companion object {
        const val PREVIOUSLY_OPENED_FRAGMENT = "PREVIOUSLY_OPENED_FRAGMENT"

        @JvmStatic
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_setting

    @Inject
    lateinit var settingViewModelFactory: SettingViewModelFactory

    override val viewModel: SettingViewModel by viewModels {
        SavedStateViewModelFactory(settingViewModelFactory, this)
    }

    private val sharedViewModel: SharedMainViewModel by activityViewModels()

    private lateinit var batteryStateReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setupBatteryStateObserver()
        setupSelectedLanguageObserver()
        restorePreviouslyOpenedFragment(savedInstanceState)
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.sharedViewModel = sharedViewModel
        viewDataBinding.fragment = this
    }

    private fun setupBatteryStateObserver() {
        val batteryStateView = viewDataBinding.settingFragmentBatteryRow

        viewModel.isBatteryChargingLiveData.observe(viewLifecycleOwner, { isCharging ->
            if (isCharging != null) {
                val iconTint = when (isCharging) {
                    true -> ContextCompat.getColor(requireContext(), R.color.battery_connected)
                    false -> ContextCompat.getColor(requireContext(), R.color.battery_disconnected)
                }
                batteryStateView.changeIconTint(iconTint)
            }
        })

        viewModel.batteryStatusLiveData.observe(viewLifecycleOwner, { status ->
            if (status != null) {
                batteryStateView.changeMainText(getString(status))
            }
        })

        viewModel.batteryPercentLiveData.observe(viewLifecycleOwner, { percentage ->
            if (percentage != null) {
                batteryStateView.changeAdditionalText(percentage)
            }
        })
    }

    private fun setupSelectedLanguageObserver() {
        viewModel.currentLanguageLiveData.observe(viewLifecycleOwner, {languageIndex ->
            viewDataBinding.settingFragmentLanguageRow.changeAdditionalText(
                when (LanguageType.values()[languageIndex]) {
                LanguageType.ENGLISH -> "ENG"
                LanguageType.UKRAINIAN -> "UKR"
            })
        })
    }

    private fun restorePreviouslyOpenedFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            when (savedInstanceState.getString(PREVIOUSLY_OPENED_FRAGMENT)) {
                ProfileFragment::class.java.name -> openProfileFragment()
                TestFragment::class.java.name -> openTestFragment()
                else -> Unit
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        parentFragmentManager.fragments.lastOrNull()?.let {
            outState.putString(PREVIOUSLY_OPENED_FRAGMENT, it::class.java.name)
        }
        super.onSaveInstanceState(outState)
    }

    fun openTestFragment() {
        val testFragment = TestFragment.newInstance(4, "TEST_STRING")
        parentFragmentManager.beginTransaction()
            .replace(R.id.setting_fragment_container, testFragment)
            .addToBackStack(null)
            .commit()
    }

    fun openProfileFragment() {
        val profileFragment = ProfileFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.setting_fragment_container, profileFragment)
            .addToBackStack(null)
            .commit()
    }

    fun openLanguagePicker() {
        LanguageListBottomSheetDialogFragment.newInstance(
            viewModel.currentLanguageLiveData.value ?: 0
        ).show(childFragmentManager, "LanguageDialog")
    }

    override fun onStart() {
        super.onStart()

        registerBatteryStatusReceiver()
    }

    private fun registerBatteryStatusReceiver() {
        batteryStateReceiver = BatteryStateReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        activity?.registerReceiver(batteryStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        activity?.unregisterReceiver(batteryStateReceiver)
    }

    override fun updateBatteryState(batteryState: BatteryStateHolder) {
        viewDataBinding.viewModel?.updateBatteryState(batteryState)
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        when (type) {
            LanguageDialogType -> {
                when (buttonType) {
                    ItemListDialogButton -> {
                        changeLanguage(data as? LanguageType)
                    }
                }
            }
        }
    }

    private fun changeLanguage(chosenLanguage: LanguageType?) {
        viewModel.changeLanguage(chosenLanguage)
        activity?.recreate()
    }
}