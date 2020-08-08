package com.test.thecocktaildb.dataBinding.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bind:iv_imageUrl")
fun ImageView.loadImage(url: String?) =
    url?.let { Glide.with(this.context).load(url).into(this) }