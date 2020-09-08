package com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.impl

import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.source.HeaderItemUserActionListener
import timber.log.Timber

class HeaderItemUserActionListenerImpl() : HeaderItemUserActionListener {

    override fun onHeaderClicked(header: String) {
        Timber.i("OnHeaderClicked $header")
    }
}