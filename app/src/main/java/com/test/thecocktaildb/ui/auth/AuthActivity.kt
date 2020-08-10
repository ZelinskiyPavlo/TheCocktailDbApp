package com.test.thecocktaildb.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktail.MainActivity
import com.test.thecocktaildb.ui.dialog.RegularDialogFragment
import com.test.thecocktaildb.util.AuthViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override val contentLayoutResId: Int = R.layout.activity_auth

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    val viewModel: AuthViewModel by viewModels {
        SavedStateViewModelFactory(authViewModelFactory, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setWhiteSpaceFilter()
        viewModel.setInitialText()
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        dataBinding.viewModel = viewModel
        dataBinding.activity = this
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
        view?.let { v ->
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        if (viewModel.isLoginDataValidLiveData.value == true) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            finish()
        } else {
            showErrorDialog()
        }
    }

    private fun showErrorDialog() {
        RegularDialogFragment.newInstance {
            titleText = "Sign in error"
            rightButtonText = "Ok"
            descriptionText = "Looks like you provided wrong login or password"
        }.show(supportFragmentManager, "SignInErrorDialog")
    }
}