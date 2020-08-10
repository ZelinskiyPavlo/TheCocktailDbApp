package com.test.thecocktaildb.util.batterystate

data class BatteryStateHolder(
    val lowBatteryWarningLevel: Int?,
    val batteryStatus: Boolean?,
    val batteryPercent: Int?,
    val isCharging: Boolean?
)