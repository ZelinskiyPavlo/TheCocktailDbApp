package com.test.thecocktaildb.util.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.test.thecocktaildb.ui.auth.AuthActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit

class OnBootService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand of onBootService")
        Toast.makeText(applicationContext, "On device boot message", Toast.LENGTH_SHORT)
            .show()

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