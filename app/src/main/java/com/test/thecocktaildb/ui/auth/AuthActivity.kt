package com.test.thecocktaildb.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktail.CocktailsActivity
import com.test.thecocktaildb.ui.dialog.RegularDialogFragment
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override val contentLayoutResId: Int = R.layout.activity_auth

    private val viewModel: AuthViewModel by viewModels()

    private val login = "SomeLogin"
    private val password = "123456a"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setupObserver()
        setWhiteSpaceFilter()
        viewModel.setInitialText()
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        dataBinding.viewModel = viewModel
        dataBinding.activity = this
    }

    private fun setupObserver() {
        viewModel.clearErrorTextColorEventLiveData.observe(this, Observer {
            changeEditTextColorToRed(false)
        })
    }

    private fun setWhiteSpaceFilter() {
        val whiteSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            source.filterNot { char -> char.isWhitespace() }
        }

        login_edit_text.filters = login_edit_text.filters + whiteSpaceFilter
        password_edit_text.filters = password_edit_text.filters + whiteSpaceFilter
    }

    fun onLoginButtonClicked() {
        val view = this.currentFocus
        when {
            viewModel.errorLoginTextColorLiveData.value == true -> {
                changeEditTextColorToRed(true)
                dataBinding.loginEditText.requestFocus()
                return
            }
            viewModel.errorPasswordTextColorLiveData.value == true -> {
                changeEditTextColorToRed(true)
                dataBinding.passwordEditText.requestFocus()
                return
            }
        }
        view?.let { v ->
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        if (viewModel.loginInputLiveData.value == login
            && viewModel.passwordInputLiveData.value == password) {
            val intent = Intent(this, CocktailsActivity::class.java)
            this.startActivity(intent)
            finish()
        } else {
            showErrorDialog()
        }
    }

    private fun showErrorDialog() {
        RegularDialogFragment.newInstance {
            titleText = getString(R.string.dialog_sign_in_error_title)
            descriptionText = getString(R.string.dialog_sign_in_error_description)
            rightButtonText = getString(R.string.dialog_sign_in_error_accept)
        }.show(supportFragmentManager, "SignInErrorDialog")
    }

    private fun changeEditTextColorToRed(isColorRed: Boolean) {
        val errorTextColor = ContextCompat.getColor(this, R.color.error_text)
        val regularTextColor = ContextCompat.getColor(this, R.color.text_default)
        if (isColorRed) {
            if (viewModel.errorLoginTextColorLiveData.value == true)
                dataBinding.loginEditText.setTextColor(errorTextColor)
            if (viewModel.errorPasswordTextColorLiveData.value == true)
                dataBinding.passwordEditText.setTextColor(errorTextColor)
        } else {
            dataBinding.loginEditText.setTextColor(regularTextColor)
            dataBinding.passwordEditText.setTextColor(regularTextColor)
        }
    }
}