package com.test.thecocktaildb.ui.cocktail.host

import androidx.lifecycle.*
import com.test.thecocktaildb.dataBinding.adapter.Page
import com.test.thecocktaildb.util.batterystate.BatteryStateCacheHolder
import com.test.thecocktaildb.util.batterystate.BatteryStateHolder
import javax.inject.Inject

class HostViewModel @Inject constructor(): ViewModel() {

    val currentPageLiveData = MutableLiveData<Page>().apply { value = Page.HistoryPage }

    private val _batteryPercentLiveData = MutableLiveData<String>()
    val batteryPercentLiveData: LiveData<String> = _batteryPercentLiveData

    private val _batteryStatusLiveData = MutableLiveData<Boolean>()
    val batteryStatusLiveData: LiveData<String> =
        Transformations.map(_batteryStatusLiveData) { if (it) "BATTERY_OKAY" else "BATTERY_LOW" }

    private val _isBatteryChargingLiveData = MutableLiveData<Boolean>()
    val isBatteryChargingLiveData: LiveData<Boolean> = _isBatteryChargingLiveData

    private val batteryStateCache = BatteryStateCacheHolder()

    val viewPagerLiveData = MediatorLiveData<Unit>().apply {

        addSource(currentPageLiveData){
            value = Unit
        }
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