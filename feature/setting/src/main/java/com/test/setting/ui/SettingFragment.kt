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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.DialogButton
import com.test.presentation.ui.dialog.DialogType
import com.test.presentation.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.setting.R
import com.test.setting.api.SettingNavigationApi
import com.test.setting.callback.BatteryStateCallback
import com.test.setting.databinding.FragmentSettingBinding
import com.test.setting.factory.SettingViewModelFactory
import com.test.setting.model.BatteryStateHolder
import com.test.setting.receiver.BatteryStateReceiver
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    internal lateinit var settingViewModelFactory: SettingViewModelFactory

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
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isBatteryChargingFlow.onEach { isCharging ->
                    val iconTint = when (isCharging) {
                        true -> ContextCompat.getColor(requireContext(), R.color.battery_connected)
                        false -> ContextCompat.getColor(requireContext(), R.color.battery_disconnected)
                    }
                    viewDataBinding.settingFragmentBatteryRow.changeIconTint(iconTint)
                }.launchIn(this)

                viewModel.batteryStatusFlow.onEach { status ->
                    viewDataBinding.settingFragmentBatteryRow.changeMainText(getString(status))
                }.launchIn(this)

                viewModel.batteryPercentFlow.onEach { percentage ->
                    viewDataBinding.settingFragmentBatteryRow.changeAdditionalText(percentage)
                }.launchIn(this)
            }
        }
    }

    // TODO: 21.02.2021 Bring back change language feature when google release appcompat lib with
    //  bug fix for locales. See previous commits for previously used code for showing language
    //  change dialog and handling user input.

    // TODO: 11.10.2021 Maybe add feature to switch to dark theme too?

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