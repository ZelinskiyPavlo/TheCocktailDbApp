package com.test.presentation.adapter.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

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