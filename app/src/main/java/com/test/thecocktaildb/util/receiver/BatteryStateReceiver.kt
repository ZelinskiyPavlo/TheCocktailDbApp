package com.test.thecocktaildb.util.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.test.thecocktaildb.ui.cocktailScreen.callback.BatteryStateCallback
import com.test.thecocktaildb.util.BatteryStateHolder

class BatteryStateReceiver(
    private val batteryStateCallback: BatteryStateCallback
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, batteryInfo: Intent?) {
        val lowBatteryWarningLevel = lowBatteryWarningLevel(context)
        var batteryStatus: Boolean? = null
        var isCharging: Boolean? = null
        var batteryPct: Float? = null

        when (batteryInfo?.action) {
            Intent.ACTION_BATTERY_LOW -> batteryStatus = false
            Intent.ACTION_BATTERY_OKAY -> batteryStatus = true
            Intent.ACTION_BATTERY_CHANGED -> {
                val status: Int = batteryInfo.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                        || status == BatteryManager.BATTERY_STATUS_FULL
                batteryPct = batteryInfo.let { intent ->
                    val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    level * 100 / scale.toFloat()
                }
            }
        }

        val batteryStateHolder = BatteryStateHolder(
            lowBatteryWarningLevel, batteryStatus, batteryPct?.toInt(), isCharging
        )
        batteryStateCallback.updateBatteryState(batteryStateHolder)
    }

    @SuppressLint("PrivateApi")
    fun lowBatteryWarningLevel(context: Context?): Int {
        try {
            val clazz = Class.forName("com.android.internal.R\$integer")
            val field = clazz.getDeclaredField("config_lowBatteryWarningLevel")
            field.isAccessible = true
            return context?.resources?.getInteger(field.getInt(null)) ?: -1
        } catch (e: Exception) {
            when (e) {
                is ClassNotFoundException -> {
                }
                is NoSuchFieldException -> {
                }
                is IllegalAccessException -> {
                }
                else -> throw IllegalStateException("unexpected exception caught")
            }
        }
        return -1
    }
}