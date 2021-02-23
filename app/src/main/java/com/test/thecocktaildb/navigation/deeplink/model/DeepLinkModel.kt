package com.test.thecocktaildb.navigation.deeplink.model

import com.test.thecocktaildb.navigation.deeplink.DeepLinkType

class DeepLinkModel(
    val deepLinkType: DeepLinkType,
    val cocktailId: Long? = null
)