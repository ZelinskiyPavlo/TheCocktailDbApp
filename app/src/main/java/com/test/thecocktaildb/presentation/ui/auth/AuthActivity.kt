package com.test.thecocktaildb.presentation.ui.auth

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.test.thecocktaildb.R
import com.test.thecocktaildb.core.common.firebase.DynamicLink
import com.test.thecocktaildb.core.common.firebase.Fcm
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.presentation.callback.NotificationCallback
import com.test.thecocktaildb.presentation.ui.base.BaseActivity
import com.test.thecocktaildb.util.locale.setLocale
import com.test.thecocktaildb.util.receiver.NotificationReceiver
import timber.log.Timber

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>(), NotificationCallback {

    override val contentLayoutResId: Int = R.layout.activity_auth

    private lateinit var navController: NavController

    override val viewModel: AuthViewModel by viewModels()

    private lateinit var notificationReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        initNavController()

        fetchDataFromFirebase()
    }

    private fun initNavController() {
        navController = findNavController(R.id.auth_nav_host_fragment)
    }

    private fun fetchDataFromFirebase() {
        fetchDataFromRemoteConfig()
        getFcmToken()
        handleNotification()
        handleDynamicLink()
    }


    private fun fetchDataFromRemoteConfig() {
        with(Firebase.remoteConfig) {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
        }

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.i("Remote config fetch successfully completed")
            } else {
                Timber.i("Remote config fetch completed with error")
            }
        }
    }

    private fun getFcmToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.i("Error occurred when retrieving fcm token")
                return@addOnCompleteListener
            }

            val token = task.result.token
            Timber.i("Fcm token $token")
        }
    }

    override fun onStart() {
        super.onStart()

        notificationReceiver = NotificationReceiver(this)
        val intentFilter = IntentFilter().apply {
            addAction(Fcm.ACTION_NEW_NOTIFICATION)
        }
        registerReceiver(notificationReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(notificationReceiver)
    }

    private fun handleNotification() {
        val notificationType = intent?.extras?.getString(Fcm.EXTRA_KEY_NOTIFICATION_TYPE)
        val cocktailId = intent?.extras?.getString(Fcm.EXTRA_KEY_COCKTAIL_ID)

        if (notificationType != null)
            viewModel.firebaseData = Pair(notificationType, cocktailId)
    }

    private fun handleDynamicLink() {
        intent.data?.let { uri ->
            val targetLink = uri.getQueryParameter("link")
            val type: String
            var id: String? = null

            if (targetLink!!.contains("&${DynamicLink.COCKTAIL_ID}=")) {
                type = DynamicLink.TYPE_COCKTAIL_DETAIL
                id = targetLink.substringAfter("&${DynamicLink.COCKTAIL_ID}=")
            } else {
                type = targetLink.substringAfter("${DynamicLink.TYPE}=")
            }

            if(type.isNotEmpty())
                viewModel.firebaseData = Pair(type, id)
        }
    }

    override fun handleBroadcastNotification(intent: Intent) {
        val notificationType = intent.extras?.getString(Fcm.EXTRA_KEY_NOTIFICATION_TYPE)
        val cocktailId = intent.extras?.getString(Fcm.EXTRA_KEY_COCKTAIL_ID)

        if (notificationType != null)
            viewModel.firebaseData = Pair(notificationType, cocktailId)
    }
}