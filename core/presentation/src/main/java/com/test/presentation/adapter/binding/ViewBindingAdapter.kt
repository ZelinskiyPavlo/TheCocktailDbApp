package com.test.presentation.adapter.binding

import android.view.View
import androidx.databinding.BindingAdapter

@Suppress("unused")
@BindingAdapter(
    value = ["bind:isVisible"]
)
fun View.isViewVisible(
    isVisible: Boolean
) {

    visibility = if (isVisible) View.VISIBLE else View.GONE
}
