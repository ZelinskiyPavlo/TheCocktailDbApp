package com.test.thecocktaildb.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

abstract class BaseActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("__________in onCreate method ")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("__________in onStart method ")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("__________in onResume method ")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("__________in onPause method ")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("__________in onStop method ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("__________in onDestroy method ")
    }
}