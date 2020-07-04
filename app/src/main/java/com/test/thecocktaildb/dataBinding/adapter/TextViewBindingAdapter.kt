package com.test.thecocktaildb.dataBinding.adapter

import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:txt_textOrGone")
fun TextView.setTextOrGone(typedText: String?) {
    text = typedText
    isGone = typedText.isNullOrEmpty()
}