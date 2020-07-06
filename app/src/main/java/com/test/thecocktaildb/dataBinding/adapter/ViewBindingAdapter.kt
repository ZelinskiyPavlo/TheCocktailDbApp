package com.test.thecocktaildb.dataBinding.adapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(
    value = ["bind:isVisible", "bind:placeholderUrl", "bind:placeholderResId", "bind:errorUrl",
        "bind:errorResId", "bind:isCrossfadeEnabled"], requireAll = false
)
fun View.isViewVisible(
    isVisible: Boolean, placeholderUrl: String?, placeholderResId: Int?, errorUrl: String?,
    errorResId: Int?, isCrossfadeEnabled: Boolean?
) {

    visibility = if (isVisible) View.VISIBLE else View.GONE
}
