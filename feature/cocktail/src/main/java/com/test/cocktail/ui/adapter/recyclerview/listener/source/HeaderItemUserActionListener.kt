package com.test.cocktail.ui.adapter.recyclerview.listener.source

import com.test.presentation.adapter.recyclerview.CustomActionListener

internal interface HeaderItemUserActionListener: CustomActionListener {

    fun onHeaderClicked(header: String)
}