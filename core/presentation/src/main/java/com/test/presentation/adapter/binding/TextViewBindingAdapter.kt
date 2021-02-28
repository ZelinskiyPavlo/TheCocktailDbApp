package com.test.presentation.adapter.binding

import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter

@Suppress("unused")
@BindingAdapter("bind:txt_textOrGone")
fun TextView.setTextOrGone(typedText: String?) {
    text = typedText
    isGone = typedText.isNullOrEmpty()
}