package com.test.thecocktaildb.ui.cocktailsScreen.callback

import com.test.thecocktaildb.util.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}