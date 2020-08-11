package com.test.thecocktaildb.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.test.thecocktaildb.dataNew.local.source.AppSettingLocalSource
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.batterystate.BatteryStateCacheHolder
import com.test.thecocktaildb.util.batterystate.BatteryStateHolder
import com.test.thecocktaildb.util.locale.LanguageType

class SettingViewModel(
    savedStateHandle: SavedStateHandle,
    settingLocalSource: AppSettingLocalSource
): BaseViewModel(savedStateHandle) {

    private val _batteryPercentLiveData = MutableLiveData<String>()
    val batteryPercentLiveData: LiveData<String> = _batteryPercentLiveData

    private val _batteryStatusLiveData = MutableLiveData<Boolean>()
    val batteryStatusLiveData: LiveData<String> =
        Transformations.map(_batteryStatusLiveData) { if (it) "BATTERY_OKAY" else "BATTERY_LOW" }

    private val _isBatteryChargingLiveData = MutableLiveData<Boolean>()
    val isBatteryChargingLiveData: LiveData<Boolean> = _isBatteryChargingLiveData

    private val _currentLanguageLiveData = settingLocalSource.currentLanguageLiveData
    val currentLanguageLiveData: LiveData<Int> = _currentLanguageLiveData

    private val batteryStateCache = BatteryStateCacheHolder()

    fun changeLanguage(chosenLanguage: LanguageType?) {
        _currentLanguageLiveData.value = chosenLanguage!!.ordinal
    }

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
            _batteryStatusLiveData.value = determineBatteryStatus()
        } else {
            _batteryStatusLiveData.value = batteryStateCache.batteryStatus
        }
        _isBatteryChargingLiveData.value = batteryStateCache.isCharging ?: false
        _batteryPercentLiveData.value = batteryStateCache.batteryPercent.toString()
    }
}