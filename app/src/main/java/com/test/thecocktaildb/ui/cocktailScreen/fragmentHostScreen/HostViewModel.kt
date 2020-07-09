package com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen

import androidx.lifecycle.*
import com.test.thecocktaildb.data.CocktailsRepository
import com.test.thecocktaildb.dataBinding.adapter.Page
import com.test.thecocktaildb.dataNew.repository.source.CocktailRepository
import com.test.thecocktaildb.ui.base.BaseViewModel
import com.test.thecocktaildb.util.BatteryStateCacheHolder
import com.test.thecocktaildb.util.BatteryStateHolder
import com.test.thecocktaildb.util.liveDataStateHandle
import timber.log.Timber

class HostViewModel(
    stateHandle: SavedStateHandle,
    private val repository: CocktailsRepository,
    private val newRepository: CocktailRepository
) : BaseViewModel(stateHandle) {

    init {
        Timber.i("Test ${newRepository.cocktailListLiveData.value}")
    }

    val currentPageLiveData by liveDataStateHandle(initialValue = Page.HistoryPage)

    private val _batteryPercentLiveData = MutableLiveData<String>()
    val batteryPercentLiveData: LiveData<String> = _batteryPercentLiveData

    private val _batteryStatusLiveData = MutableLiveData<Boolean>()
    val batteryStatusLiveData: LiveData<String> =
        Transformations.map(_batteryStatusLiveData) { if (it) "BATTERY_OKAY" else "BATTERY_LOW" }

    private val _isBatteryChargingLiveData = MutableLiveData<Boolean>()
    val isBatteryChargingLiveData: LiveData<Boolean> = _isBatteryChargingLiveData

    private val batteryStateCache = BatteryStateCacheHolder()

    val viewPagerLiveData = MediatorLiveData<Unit>().apply {

        addSource(currentPageLiveData) {
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