package com.test.cocktail.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentation.ui.auth.AuthActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit

class OnBootService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(
            applicationContext,
            getString(R.string.service_on_boot_device_toast),
            Toast.LENGTH_SHORT
        ).show()

        Observable
            .timer(3, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onComplete = {
                Intent(applicationContext, AuthActivity::class.java).apply {
                    startActivity(this)
                }
            }).dispose()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}