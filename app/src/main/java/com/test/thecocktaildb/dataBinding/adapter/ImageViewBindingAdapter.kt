package com.test.thecocktaildb.dataBinding.adapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.test.thecocktaildb.R

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

@BindingAdapter("bind:iv_batteryBar_tint")
fun ImageView.setTint(isCharging: Boolean) {
    if (isCharging) {
        val colorConnected = ContextCompat.getColor(this.context, R.color.battery_connected)
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorConnected))
    } else {
        val colorDisconnected = ContextCompat.getColor(this.context, R.color.battery_disconnected)
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorDisconnected))
    }
}