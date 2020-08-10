package com.test.thecocktaildb.dataBinding.adapter

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(
    value = ["bind:iv_imageUrl", "bind:placeholderUrl", "bind:placeholderResId",
        "bind:errorUrl", "bind:errorResId", "bind:isCrossFadeEnabled"], requireAll = false
)
fun ImageView.loadImage(
    url: String?, placeholderUrl: Drawable?, placeholderResId: Int?, errorUrl: Drawable?,
    errorResId: Int?, isCrossFadeEnabled: Boolean?
) {
    url?.let {_ ->
        val imageBuilder = Glide.with(this.context).load(url)

        placeholderUrl?.let { imageBuilder.placeholder(it) }
        placeholderResId?.let { imageBuilder.placeholder(it) }

        errorUrl?.let { imageBuilder.error(it) }
        errorResId?.let { imageBuilder.error(it) }

        if (isCrossFadeEnabled == true) imageBuilder.transition(DrawableTransitionOptions.withCrossFade())

        imageBuilder.into(this)
    }
}

@BindingAdapter("bind:iv_imageUrl")
fun ImageView.loadImage(url: String?) =
    url?.let { Glide.with(this.context).load(url).into(this) }