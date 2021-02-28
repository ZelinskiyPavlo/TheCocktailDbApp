package com.test.presentation.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.core.content.res.use
import com.test.presentation.R
import kotlinx.android.synthetic.main.widget_custom_toolbar.view.*

class CustomToolbar : RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(attrs)
    }

    lateinit var backButton: ImageButton
    lateinit var title: TextView
    lateinit var primaryOption: ImageButton
    lateinit var secondaryOption: ImageButton

    private fun init(attrs: AttributeSet? = null) {
        setWillNotDraw(false)
        inflate(context, R.layout.widget_custom_toolbar, this)

        backButton = toolbar_back_button
        title = toolbar_title
        primaryOption = toolbar_main_option_button
        secondaryOption = toolbar_secondary_option_button

        context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar).use { typedArray ->
            title.text = typedArray.getText(R.styleable.CustomToolbar_toolbar_title_text)
            typedArray.setupIcon(
                primaryOption, R.styleable.CustomToolbar_toolbar_primary_option_icon
            )
            typedArray.setupIcon(
                secondaryOption, R.styleable.CustomToolbar_toolbar_secondary_option_icon
            )
            backButton.visibility =
                if (typedArray.getBoolean(
                        R.styleable.CustomToolbar_toolbar_back_button_visible, false
                    )
                ) View.VISIBLE
                else View.GONE
        }
    }

    private fun TypedArray.setupIcon(view: ImageButton, @StyleableRes src: Int) {
        if (getDrawable(src) != null) {
            view.visibility = View.VISIBLE
            view.setImageDrawable(getDrawable(src))
        } else {
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