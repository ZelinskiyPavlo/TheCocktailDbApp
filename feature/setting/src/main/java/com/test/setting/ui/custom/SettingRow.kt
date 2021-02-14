package com.test.setting.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.res.use
import androidx.core.widget.ImageViewCompat
import com.test.setting.R
import kotlinx.android.synthetic.main.widget_setting_row.view.*

class SettingRow(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {

    private var icon: ImageView
    private var mainText: TextView
    private var additionalText: TextView

    init {
        View.inflate(context, R.layout.widget_setting_row, this)

        icon = setting_row_view_icon
        mainText = setting_row_view_main_text
        additionalText = setting_row_view_additional_text

        context.obtainStyledAttributes(attributes, R.styleable.SettingRow).use {typedArray ->
            icon.setImageDrawable(typedArray.getDrawable(R.styleable.SettingRow_sr_icon_src))
            mainText.text = typedArray.getText(R.styleable.SettingRow_sr_main_text)
            if (typedArray.getText(R.styleable.SettingRow_sr_additional_text) != null) {
                additionalText.visibility = View.VISIBLE
                additionalText.text = typedArray.getText(R.styleable.SettingRow_sr_additional_text)
            } else {
                additionalText.visibility = View.GONE
            }
        }
    }

    fun changeMainText(newText: String) {
        mainText.text = newText
    }

    fun changeAdditionalText(newText: String) {
        with(additionalText) {
            if (visibility == View.GONE) visibility = View.VISIBLE
            text = newText
        }
    }

    fun changeIcon(newDrawable: Drawable) {
        icon.setImageDrawable(newDrawable)
    }

    fun changeIconTint(@IdRes tint: Int) {
        ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(tint))
    }
}