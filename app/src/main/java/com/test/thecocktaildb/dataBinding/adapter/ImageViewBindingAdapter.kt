package com.test.thecocktaildb.dataBinding.adapter

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.test.thecocktaildb.R

@BindingAdapter("bind:iv_imageUrl")
fun ImageView.loadImage(url: String?) =
    url?.let { Glide.with(this.context).load(url).into(this) }

@BindingAdapter("bind:iv_batteryBar_tint")
fun ImageView.setTint(isCharging: Boolean) {
    if (isCharging) {
        val colorConnected = this.context.getColor(R.color.battery_connected)
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorConnected))
    } else {
        val colorDisconnected = this.context.getColor(R.color.battery_disconnected)
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorDisconnected))
    }
}