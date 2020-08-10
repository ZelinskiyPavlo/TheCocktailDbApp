package com.test.thecocktaildb.dataBinding.adapter

import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager2.widget.ViewPager2
import com.test.thecocktaildb.R

@BindingAdapter("bind:vp_page")
fun ViewPager2.setCurrentPage(page: Int) {
    if (currentItem != page) setCurrentItem(page, true)
}

@InverseBindingAdapter(attribute = "bind:vp_page", event = "bind:vp_pageAttrChanged")
fun ViewPager2.getCurrentPage(): Int {
    return currentItem
}

@BindingAdapter("bind:vp_pageAttrChanged")
fun ViewPager2.pageChangeListener(pageAttrChanged: InverseBindingListener?) {
    val newListener: ViewPager2.OnPageChangeCallback?

    newListener = if (pageAttrChanged == null) {
        null
    } else {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageAttrChanged.onChange()
            }
        }
    }

    val oldListener =
        ListenerUtil.trackListener(this, newListener, R.id.viewpager_page_change_listener)
    oldListener?.let { unregisterOnPageChangeCallback(oldListener) }

    newListener?.let { registerOnPageChangeCallback(newListener) }
}

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