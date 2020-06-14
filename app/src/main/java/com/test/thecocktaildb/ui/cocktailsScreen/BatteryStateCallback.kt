package com.test.thecocktaildb.ui.cocktailsScreen

import com.test.thecocktaildb.util.BatteryStateHolder

interface BatteryStateCallback {

    fun updateBatteryState(batteryState: BatteryStateHolder)
}