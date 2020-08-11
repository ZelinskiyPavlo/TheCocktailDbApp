package com.test.thecocktaildb.presentation.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StyleableRes
import com.test.thecocktaildb.R
import kotlinx.android.synthetic.main.widget_custom_toolbar.view.*

class CustomToolbar(context: Context, attributes: AttributeSet) :
    RelativeLayout(context, attributes) {

    var backButton: ImageButton
    var title: TextView
    var primaryOption: ImageButton
    var secondaryOption: ImageButton


    init {
        View.inflate(context, R.layout.widget_custom_toolbar, this)

        backButton = toolbar_back_button
        title = toolbar_title
        primaryOption = toolbar_main_option_button
        secondaryOption = toolbar_secondary_option_button

        with(context.obtainStyledAttributes(attributes, R.styleable.CustomToolbar)) {
            title.text = getText(R.styleable.CustomToolbar_title_text)
            setupIcon(primaryOption, R.styleable.CustomToolbar_primary_option_icon)
            setupIcon(secondaryOption, R.styleable.CustomToolbar_secondary_option_icon)
            backButton.visibility =
                if(getBoolean(R.styleable.CustomToolbar_back_button_visible, false))
                    View.VISIBLE
                else
                    View.GONE

            recycle()
        }


    }

    private fun TypedArray.setupIcon(view: ImageButton, @StyleableRes src: Int) {
        if(getDrawable(src) != null) {
            view.visibility = View.VISIBLE
            view.setImageDrawable(getDrawable(src))
        } else{
            view.visibility = View.GONE
        }
    }

    fun changePrimaryOptionImage(newImage: Int) {
        primaryOption.setImageResource(newImage)
    }

    fun changeSecondaryOptionImage(newImage: Int) {
        secondaryOption.setImageResource(newImage)
    }
}