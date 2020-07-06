package com.test.thecocktaildb.dataBinding.adapter

import androidx.databinding.*
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("bind:vp_page")
fun ViewPager2.setCurrentPage(page: Int) {
    if (currentItem != page) setCurrentItem(page, true)
}

@InverseBindingAdapter(attribute = "bind:vp_page", event = "bind:vp_pageAttrChanged")
fun ViewPager2.getCurrentPage(): Int {
    return currentItem
}

@BindingAdapter(/*"bind:vp_onPageChanged", */"bind:vp_pageAttrChanged")
fun ViewPager2.linkerTemp(
    inverseListener: InverseBindingListener
) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            inverseListener.onChange()
        }
    })
}

interface pageChangeListener

object Converter {

    @BindingConversion
    @JvmStatic
    fun convertPageToInt(page: Page): Int = page.ordinal

    @InverseMethod("convertPageToInt")
    @JvmStatic
    fun convertIntToPage(page: Int): Page = Page.values()[page]

}

enum class Page {
    HistoryPage,
    FavoritePage
}