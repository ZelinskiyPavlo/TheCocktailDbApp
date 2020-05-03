package com.test.thecocktaildb.utils

import com.test.thecocktaildb.utils.CustomActionListener

interface CocktailsItemUserActionListener: CustomActionListener {

    fun onItemClicked()
}