package com.test.presentation.extension

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

fun TextView.addLinkedText(linkedText: String?, action: () -> Unit) {

    if(linkedText != null) {

        val startIndex = text.toString().indexOf(linkedText, ignoreCase = true)
        val endIndex = startIndex + linkedText.length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                action()
            }
        }

        val spannableString = SpannableString(text)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }
}