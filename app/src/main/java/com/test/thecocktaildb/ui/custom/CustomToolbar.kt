package com.test.thecocktaildb.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
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

        val attrs = context.obtainStyledAttributes(attributes, R.styleable.CustomToolbar)

        title.text = attrs.getText(R.styleable.CustomToolbar_title_text)
        primaryOption.setImageDrawable(attrs.getDrawable(R.styleable.CustomToolbar_primary_option_icon))
        secondaryOption.setImageDrawable(attrs.getDrawable(R.styleable.CustomToolbar_secondary_option_icon))

        attrs.recycle()
    }

    fun changePrimaryOptionImage(newImage: Int) {
        primaryOption.setImageResource(newImage)
    }

    fun changeSecondaryOptionImage(newImage: Int) {
        secondaryOption.setImageResource(newImage)
    }
}