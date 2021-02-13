package com.test.cocktail.ui.adapter.recyclerview.listener.impl

import com.test.cocktail.ui.adapter.recyclerview.listener.source.HeaderItemUserActionListener
import timber.log.Timber

class HeaderItemUserActionListenerImpl : HeaderItemUserActionListener {

    override fun onHeaderClicked(header: String) {
        Timber.i("OnHeaderClicked $header")
    }
}