package com.test.setting.ui

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.DialogButton
import com.test.presentation.ui.dialog.DialogType
import com.test.presentation.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.setting.R
import com.test.setting.callback.BatteryStateCallback
import com.test.setting.databinding.FragmentSettingBinding
import com.test.setting.factory.SettingViewModelFactory
import com.test.setting.model.BatteryStateHolder
import com.test.setting.navigation.SettingNavigationApi
import com.test.setting.receiver.BatteryStateReceiver
import javax.inject.Inject

class SettingFragment : BaseFragment<FragmentSettingBinding>(), BatteryStateCallback,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    companion object {
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

    @Inject
    lateinit var settingNavigator: SettingNavigationApi

    private lateinit var batteryStateReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupBatteryStateObserver()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
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

    // TODO: 21.02.2021 Bring back change language feature when google release appcompat lib with
    //  bug fix for locales. See previous commits for previously used code for showing language
    //  change dialog and handling user input.

    private fun configureViewFromRemoteConfig() {
//        sharedViewModel.showNavTitlesViewVisibilityLiveData.observe(viewLifecycleOwner, {
//            viewDataBinding.settingFragmentShowNavTitles.visibility =
//                if (it) View.VISIBLE
//                else View.GONE
//        })
    }

    fun openProfileFragment() {
        settingNavigator.toProfile()
    }

    fun openCubeView() {
        settingNavigator.toCube()
    }

    fun openRangeSeekBarFragment() {
        settingNavigator.toSeekBar()
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
        viewModel.updateBatteryState(batteryState)
    }
}