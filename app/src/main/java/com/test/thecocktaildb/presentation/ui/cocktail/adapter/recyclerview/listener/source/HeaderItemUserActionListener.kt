package com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.listener.source

import com.test.thecocktaildb.presentation.ui.adapter.recyclerview.CustomActionListener

interface HeaderItemUserActionListener: CustomActionListener {

    fun onHeaderClicked(header: String)
}