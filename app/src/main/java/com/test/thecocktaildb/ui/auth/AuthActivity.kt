package com.test.thecocktaildb.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {

    private val login = "SomeLogin"
    private val password = "123456a"

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            invalidateAuthData()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        login_edit_text.addTextChangedListener(textWatcher)
        password_edit_text.addTextChangedListener(textWatcher)

        login_button.setOnClickListener {

            val view = this.currentFocus
            view?.let { v ->
                val imm =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }

            val intent = Intent(this, CocktailsActivity::class.java)
            this.startActivity(intent)
        }

        invalidateAuthData()
    }

    private fun invalidateAuthData() {
        val typedLogin = login_edit_text.text.toString()
        val typedPassword = password_edit_text.text.toString()
        login_button.isEnabled = false

        if (typedLogin.any { it.isWhitespace() }) {
            login_edit_text.setText(typedLogin.replace(" ", ""))
            login_edit_text.setSelection(login_edit_text.text?.length ?: 0)
        }

        if (typedPassword.any { it.isWhitespace() }) {
            password_edit_text.setText(typedPassword.replace(" ", ""))
            password_edit_text.setSelection(password_edit_text.text?.length ?: 0)
        }

        if (typedLogin.length < 6) login_edit_text_layout.error = "Login too short"
        else login_edit_text_layout.isErrorEnabled = false
        if (typedPassword.length < 6) password_edit_text_layout.error = "Password too short"
        else password_edit_text_layout.isErrorEnabled = false
        if (typedPassword.any { it.isDigit() } && typedPassword.any { it.isLetter() })
            password_edit_text_layout.isErrorEnabled = false
        else password_edit_text_layout.error = "Password doesn't suit requirements"

        login_button.isEnabled = typedLogin == login && typedPassword == password
    }
}