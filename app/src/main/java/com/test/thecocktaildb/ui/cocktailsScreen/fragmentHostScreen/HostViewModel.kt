package com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.test.thecocktaildb.util.BatteryStateCacheHolder
import com.test.thecocktaildb.util.BatteryStateHolder
import javax.inject.Inject

class HostViewModel @Inject constructor(): ViewModel() {

    private val _batteryPercent = MutableLiveData<String>()
    val batteryPercent: LiveData<String> = _batteryPercent

    private val _batteryStatus = MutableLiveData<Boolean>()
    val batteryStatus: LiveData<String> =
        Transformations.map(_batteryStatus) { if (it) "BATTERY_OKAY" else "BATTERY_LOW" }

    private val _isBatteryCharging = MutableLiveData<Boolean>()
    val isBatteryCharging: LiveData<Boolean> = _isBatteryCharging

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
            _batteryStatus.value = determineBatteryStatus()
        } else {
            _batteryStatus.value = batteryStateCache.batteryStatus
        }
        _isBatteryCharging.value = batteryStateCache.isCharging ?: false
        _batteryPercent.value = batteryStateCache.batteryPercent.toString()
    }
}