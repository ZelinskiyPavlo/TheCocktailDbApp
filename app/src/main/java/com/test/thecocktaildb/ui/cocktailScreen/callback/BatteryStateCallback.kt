package com.test.thecocktaildb.ui.cocktailScreen.callback

import com.test.thecocktaildb.util.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}