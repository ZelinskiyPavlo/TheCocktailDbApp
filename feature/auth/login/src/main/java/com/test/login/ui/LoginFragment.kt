package com.test.login.ui

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.test.login.R
import com.test.login.api.LoginNavigationApi
import com.test.login.databinding.FragmentLoginBinding
import com.test.login.factory.LoginViewModelFactory
import com.test.presentation.extension.addLinkedText
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.RegularDialogFragment
import com.test.presentation.util.EventObserver
import javax.inject.Inject

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_login

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    override val viewModel: LoginViewModel by viewModels {
        SavedStateViewModelFactory(loginViewModelFactory, this)
    }

//    private val sharedViewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var loginNavigator: LoginNavigationApi

    private val errorTextColor
            by lazy { ContextCompat.getColor(requireActivity(), R.color.error_text) }
    private val regularTextColor
            by lazy { ContextCompat.getColor(requireActivity(), R.color.text_default) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setWhiteSpaceFilter()
        addLinkedText()
        setupObserver()
        viewModel.setInitialText()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    private fun setWhiteSpaceFilter() {
        val whiteSpaceFilter = InputFilter { source, _, _, _, _, _ ->
            source.filterNot { char -> char.isWhitespace() }
        }
        with(viewDataBinding) {
            loginEditText.filters = loginEditText.filters + whiteSpaceFilter
            passwordEditText.filters = passwordEditText.filters + whiteSpaceFilter
        }
    }

    private fun addLinkedText() {
        viewDataBinding.registerSignInTv
            .addLinkedText(this.getString(R.string.all_sign_up)) { navigateToRegisterFragment() }
    }

    private fun setupObserver() {
        viewModel.clearErrorTextColorEventLiveData.observe(viewLifecycleOwner, {
            clearErrors()
        })

        viewModel.loginEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver { isLoggedSuccessful ->
                if (isLoggedSuccessful) navigateToTabHost()
            })

        viewModel.isDataValidLiveData.observe(viewLifecycleOwner, {isAvailable ->
            viewDataBinding.loginButton.isEnabled = isAvailable
        })
    }

    fun onLoginButtonClicked() {
        if (isTypedDataContainErrors()) {
            showInvalidInputDialog()
            return
        }

        val view = activity?.currentFocus
        view?.let { v ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        viewModel.loginUser()
    }

    private fun navigateToRegisterFragment() {
        loginNavigator.toRegister()
    }

    private fun navigateToTabHost() {
//        val (notificationType, cocktailId) = sharedViewModel.firebaseData
        loginNavigator.toTabHost()
    }

    private fun isTypedDataContainErrors(): Boolean {
        if(viewModel.isDataValidLiveData.value == null) return true
        var error = false

        fun markFieldAsError(textField: TextInputEditText) {
            textField.setTextColor(errorTextColor)
            textField.requestFocus()
            error = true
        }

        with(viewModel) {
            if (wrongPassword)
                markFieldAsError(viewDataBinding.passwordEditText)
            if (wrongEmail)
                markFieldAsError(viewDataBinding.loginEditText)
        }
        return error
    }

    private fun clearErrors() {
        fun clearFieldError(textField: TextInputEditText) {
            textField.setTextColor(regularTextColor)
        }

        clearFieldError(viewDataBinding.loginEditText)
        clearFieldError(viewDataBinding.passwordEditText)
    }

    private fun showInvalidInputDialog() {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.invalid_login_input_dialog_title
            descriptionTextResId = R.string.invalid_login_input_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(childFragmentManager, "SignInErrorDialog")
    }

}