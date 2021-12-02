package com.test.setting.ui

import androidx.lifecycle.SavedStateHandle
import com.test.presentation.ui.base.BaseViewModel
import com.test.repository.source.AppSettingRepository
import com.test.setting.R
import com.test.setting.model.BatteryStateCacheHolder
import com.test.setting.model.BatteryStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class SettingViewModel(
    savedStateHandle: SavedStateHandle,
    settingRepository: AppSettingRepository
) : BaseViewModel(savedStateHandle) {

    private val _batteryPercentFlow = MutableStateFlow("")
    val batteryPercentFlow = _batteryPercentFlow.asStateFlow()

    private val _batteryStatusFlow = MutableStateFlow<Boolean?>(null)
    // TODO: 11.10.2021 Did i use R.strings in other projects?
    val batteryStatusFlow = _batteryStatusFlow.map {
        if (it == true) R.string.battery_status_battery_okay else R.string.battery_status_battery_low
    }

    private val _isBatteryChargingFlow = MutableStateFlow<Boolean?>(null)
    val isBatteryChargingFlow = _isBatteryChargingFlow.asStateFlow().filterNotNull()

    var shouldShowNavigationTitle = settingRepository.showNavigationTitle

    private val batteryStateCache = BatteryStateCacheHolder()

    fun updateBatteryState(stateHolder: BatteryStateHolder) {
        fun determineBatteryStatus(): Boolean {
            if (stateHolder.batteryPercent != null && stateHolder.lowBatteryWarningLevel != null) {
                return stateHolder.batteryPercent > stateHolder.lowBatteryWarningLevel
            }
            return false
        }

        fun cacheBatteryState() {
            stateHolder.lowBatteryWarningLevel?.let {
                batteryStateCache.lowBatteryWarningLevel = stateHolder.lowBatteryWarningLevel
            }
            stateHolder.batteryStatus?.let {
                batteryStateCache.batteryStatus = stateHolder.batteryStatus
            }
            stateHolder.batteryPercent?.let {
                batteryStateCache.batteryPercent = stateHolder.batteryPercent
            }
            stateHolder.isCharging?.let {
                batteryStateCache.isCharging = stateHolder.isCharging
            }
        }

        cacheBatteryState()

        if (batteryStateCache.batteryStatus == null) {
            _batteryStatusFlow.value = determineBatteryStatus()
        } else {
            _batteryStatusFlow.value = batteryStateCache.batteryStatus
        }
        _isBatteryChargingFlow.value = batteryStateCache.isCharging ?: false
        _batteryPercentFlow.value = batteryStateCache.batteryPercent.toString()
    }
}