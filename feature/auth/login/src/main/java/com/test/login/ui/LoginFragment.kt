package com.test.login.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputEditText
import com.test.login.R
import com.test.login.databinding.FragmentLoginBinding
import com.test.login.factory.LoginViewModelFactory
import com.test.presentation.extension.addLinkedText
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.RegularDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    internal lateinit var loginViewModelFactory: LoginViewModelFactory

    override val viewModel: LoginViewModel by viewModels {
        SavedStateViewModelFactory(loginViewModelFactory, this)
    }

    private val errorTextColor
            by lazy { ContextCompat.getColor(requireActivity(), R.color.error_text) }
    private val regularTextColor
            by lazy { ContextCompat.getColor(requireActivity(), R.color.text_default) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setWhiteSpaceFilter()
        addLinkedText()
        setupKeyboardClosing()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.onEach { event ->
                    when (event) {
                        LoginViewModel.Event.ClearErrorTextColor -> clearErrors()
                    }
                }.launchIn(this)

                viewModel.isDataValidFlow.onEach { isAvailable ->
                    viewDataBinding.loginButton.isEnabled = isAvailable
                }.launchIn(this)
            }
        }
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
            .addLinkedText(this.getString(R.string.all_sign_up)) { viewModel.navigateToRegister() }
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

    private fun isTypedDataContainErrors(): Boolean {
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

    // TODO: 27.02.2021 Extract to generic function
    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardClosing() {
        viewDataBinding.loginRootLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = when {
                    viewDataBinding.loginEditText.isFocused -> {
                        viewDataBinding.loginEditText
                    }
                    viewDataBinding.passwordEditText.isFocused -> {
                        viewDataBinding.passwordEditText
                    }
                    else -> null
                }
                if (focusedView != null) {
                    val outRect = Rect()
                    focusedView.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        focusedView.clearFocus()
                        val imm =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
            false
        }
    }
}