package com.test.thecocktaildb.navigation.deeplink

import android.net.Uri
import androidx.core.net.toUri
import com.github.terrakok.cicerone.*
import com.test.common.Event
import com.test.firebase.common.DynamicLink
import com.test.firebase.common.Fcm
import com.test.thecocktaildb.R
import com.test.thecocktaildb.navigation.deeplink.model.DeepLinkModel
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.navigation.state.NavigationStateHolder

open class DeepLinkNavigationHandler(
    private val navigator: Navigator,
    private val navigationStateHolder: NavigationStateHolder,
    private val isDirectDeepLink: Boolean
) {

    private var deepLinkModel: DeepLinkModel? = null

    fun processDeepLink(url: Uri): Boolean {
        extractType(url)
        return processDeepLink()
    }

    fun processDeepLink(notificationType: String, cocktailId: Long?) {
        extractType(notificationType, cocktailId)
        processDeepLink()
    }

    private fun processDeepLink(): Boolean {
        if (isDirectDeepLink) performNavigation()
        else saveDeepLinkModel()

        return isDirectDeepLink
    }

    private fun extractType(url: Uri) {
        val targetLink = url.getQueryParameter(DynamicLink.LINK)!!

        val notificationType =
            when (targetLink.toUri().getQueryParameter(DynamicLink.TYPE).toString()) {
                DynamicLink.TYPE_COCKTAIL -> DeepLinkType.COCKTAIL
                DynamicLink.TYPE_PROFILE -> DeepLinkType.PROFILE
                DynamicLink.TYPE_COCKTAIL_DETAIL -> DeepLinkType.COCKTAIL_DETAIL
                else -> throw IllegalArgumentException("Unknown notification query type")
            }
        val cocktailId = targetLink.toUri().getQueryParameter(DynamicLink.COCKTAIL_ID)?.toLong()

        deepLinkModel = DeepLinkModel(notificationType, cocktailId)
    }

    private fun extractType(notificationType: String, cocktailId: Long?) {
        val deepLinkType = when (notificationType) {
            Fcm.TYPE_COCKTAIL -> DeepLinkType.COCKTAIL
            Fcm.TYPE_PROFILE -> DeepLinkType.PROFILE
            Fcm.TYPE_COCKTAIL_DETAIL -> DeepLinkType.COCKTAIL_DETAIL
            Fcm.TYPE_RATE_DIALOG -> DeepLinkType.RATE_DIALOG
            else -> throw IllegalArgumentException("Unknown notification type string")
        }
        deepLinkModel = DeepLinkModel(deepLinkType, cocktailId)
    }

    private fun performNavigation() {
        when (deepLinkModel?.deepLinkType) {
            DeepLinkType.COCKTAIL -> navigateToCocktailTab()
            DeepLinkType.PROFILE -> navigateToProfile()
            DeepLinkType.COCKTAIL_DETAIL -> navigateToCocktailDetail(deepLinkModel!!.cocktailId)
            DeepLinkType.RATE_DIALOG -> TODO("Rate dialog not yet implemented")
        }
    }

    fun performNavigation(deepLinkModel: DeepLinkModel) {
        this.deepLinkModel = deepLinkModel
        performNavigation()
    }

    private fun saveDeepLinkModel() {
        navigationStateHolder.deepLinkModel = deepLinkModel
    }

    private fun navigateToCocktailTab() {
        val clearChain = BackTo(null)
        val replaceWithTabHost = Replace(Screen.tabHost())

        val commands = ArrayList<Command>().apply {
            add(clearChain)
            add(replaceWithTabHost)
        }.toTypedArray()

        navigator.applyCommands(commands)
    }

    private fun navigateToProfile() {
        navigationStateHolder.tabHostSelectedTabEvent = Event(R.id.bnv_setting_item)

        val clearChain = BackTo(null)
        val replaceWithTabHost = Replace(Screen.tabHost())
        val forwardToProfileEdit = Forward(Screen.profile(), true)

        val commands = ArrayList<Command>().apply {
            add(clearChain)
            add(replaceWithTabHost)
            add(forwardToProfileEdit)
        }.toTypedArray()

        navigator.applyCommands(commands)
    }

    private fun navigateToCocktailDetail(cocktailId: Long?) {
        cocktailId ?: return

        val clearChain = BackTo(null)
        val replaceWithTabHost = Replace(Screen.tabHost())
        val forwardToCocktailDetail = Forward(Screen.detail(cocktailId), true)

        val commands = ArrayList<Command>().apply {
            add(clearChain)
            add(replaceWithTabHost)
            add(forwardToCocktailDetail)
        }.toTypedArray()

        navigator.applyCommands(commands)
    }
}