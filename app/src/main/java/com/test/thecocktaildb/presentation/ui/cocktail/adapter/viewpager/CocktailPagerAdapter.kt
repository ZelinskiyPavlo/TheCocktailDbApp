package com.test.thecocktaildb.presentation.ui.cocktail.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CocktailPagerAdapter(
    private val fragmentsList: ArrayList<Fragment> = arrayListOf(),
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragmentsList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }


}