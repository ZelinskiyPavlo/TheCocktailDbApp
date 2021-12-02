package com.test.thecocktaildb.navigation.state

import com.test.common.Event
import com.test.thecocktaildb.navigation.deeplink.model.DeepLinkModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

/** I'm not sure if this is a correct solution. Maybe I need to use some sort of event bus.
 * Anyway, this class contain all navigation state that needs to be changed in order to
 *  perform some navigation. For example when navigating to screen from settings tab you need to
 *  switch initial tab (which is cocktail) to setting in tabHost module. We inject only such
 *  states that this module can handle through navigation interface.
 */
@Singleton
class NavigationStateHolder @Inject constructor() {
    
    var tabHostSelectedTabEvent: Event<Int>? = null

    var deepLinkModel: DeepLinkModel? = null

    private val deferredDeepLinkChannel = Channel<DeepLinkModel>(capacity = Channel.CONFLATED)
    val deferredDeepLinkFlow = deferredDeepLinkChannel.receiveAsFlow()

    fun sendDeferredDeepLinkEvent(event: DeepLinkModel) {
        deferredDeepLinkChannel.trySend(event)
    }
}