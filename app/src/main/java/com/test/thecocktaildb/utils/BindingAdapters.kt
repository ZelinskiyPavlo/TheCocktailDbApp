package com.test.thecocktaildb.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("app:items")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String) =
        Glide.with(imageView.context).load(url).into(imageView)

}