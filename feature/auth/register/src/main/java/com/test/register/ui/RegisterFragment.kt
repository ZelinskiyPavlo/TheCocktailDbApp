package com.test.register.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
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
import com.test.navigation.HasBackPressLogic
import com.test.presentation.extension.addLinkedText
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.RegularDialogFragment
import com.test.register.R
import com.test.register.api.RegisterNavigationApi
import com.test.register.databinding.FragmentRegisterBinding
import com.test.register.factory.RegisterViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(), HasBackPressLogic {

    companion object {
        @JvmStatic
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_register

    @Inject
    internal lateinit var registerViewModelFactory: RegisterViewModelFactory

    override val viewModel: RegisterViewModel by viewModels {
        SavedStateViewModelFactory(registerViewModelFactory, this)
    }

    @Inject
    lateinit var registerNavigator: RegisterNavigationApi

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
        configureTogglePasswordButton()
        setupKeyboardClosing()

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
            registerEmailEt.filters = registerEmailEt.filters + whiteSpaceFilter
            registerNameEt.filters = registerNameEt.filters + whiteSpaceFilter
            registerLastNameEt.filters = registerLastNameEt.filters + whiteSpaceFilter
            registerPasswordEt.filters = registerPasswordEt.filters + whiteSpaceFilter
            registerConfirmPasswordEt.filters = registerConfirmPasswordEt.filters + whiteSpaceFilter
        }
    }

    private fun addLinkedText() {
        viewDataBinding.registerSignInTv
            .addLinkedText(this.getString(R.string.all_sign_in)) { navigateToLoginScreen() }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.onEach { event ->
                    when (event) {
                        RegisterViewModel.Event.ClearErrorTextColor -> clearErrors()

                        RegisterViewModel.Event.ToRegister -> navigateToCocktailScreen()
                    }
                }.launchIn(this)

                viewModel.isDataValidFlow.onEach { isAvailable ->
                    viewDataBinding.registerButtonRegister.isEnabled = isAvailable
                }.launchIn(this)
            }
        }
    }

    private fun configureTogglePasswordButton() {
        viewDataBinding.registerPasswordEtl.setEndIconOnClickListener {
            val textInputMode =
                when (viewDataBinding.registerPasswordEt.transformationMethod) {
                    is PasswordTransformationMethod -> null
                    null -> PasswordTransformationMethod()
                    else -> null
                }
            viewDataBinding.registerPasswordEt.transformationMethod = textInputMode
            viewDataBinding.registerConfirmPasswordEt.transformationMethod = textInputMode

            viewDataBinding.registerPasswordEt.run {
                setSelection(text?.length ?: 0)
            }
            viewDataBinding.registerConfirmPasswordEt.run {
                setSelection(text?.length ?: 0)
            }
        }
    }

    fun onRegisterButtonClicked() {
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
        viewModel.registerUser()
    }

    private fun navigateToLoginScreen() {
        registerNavigator.toLogin()
    }

    private fun navigateToCocktailScreen() {
        registerNavigator.toTabHost()
    }

    override fun onBackPressed() {
        registerNavigator.toLogin()
    }

    private fun isTypedDataContainErrors(): Boolean {
        var error = false

        fun markFieldAsError(textField: TextInputEditText) {
            textField.setTextColor(errorTextColor)
            textField.requestFocus()
            error = true
        }

        with(viewModel) {
            if (passwordsNotMatch) {
                viewDataBinding.registerConfirmPasswordEtl.error = "Passwords needs to match"
                viewDataBinding.registerConfirmPasswordEt.requestFocus()
                error = true
            }
            if (wrongPasswordConfirm)
                markFieldAsError(viewDataBinding.registerConfirmPasswordEt)
            if (wrongPassword)
                markFieldAsError(viewDataBinding.registerPasswordEt)
            if (wrongLastName)
                markFieldAsError(viewDataBinding.registerLastNameEt)
            if (wrongName)
                markFieldAsError(viewDataBinding.registerNameEt)
            if (wrongEmail)
                markFieldAsError(viewDataBinding.registerEmailEt)

        }
        return error
    }

    private fun clearErrors() {
        fun clearFieldError(vararg textFields: TextInputEditText) {
            textFields.forEach {
                it.setTextColor(regularTextColor)
            }
        }

        with(viewDataBinding) {
            registerConfirmPasswordEtl.error = null
            clearFieldError(
                registerEmailEt, registerNameEt, registerLastNameEt, registerPasswordEt,
                registerConfirmPasswordEt
            )
        }
    }

    private fun showInvalidInputDialog() {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.invalid_register_input_dialog_title
            descriptionTextResId = R.string.invalid_register_input_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(childFragmentManager, "SignUpErrorDialog")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardClosing() {
        viewDataBinding.registerRootLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val focusedView = when {
                    viewDataBinding.registerEmailEt.isFocused -> {
                        viewDataBinding.registerEmailEt
                    }
                    viewDataBinding.registerNameEt.isFocused -> {
                        viewDataBinding.registerNameEt
                    }
                    viewDataBinding.registerLastNameEt.isFocused -> {
                        viewDataBinding.registerNameEt
                    }
                    viewDataBinding.registerPasswordEt.isFocused -> {
                        viewDataBinding.registerNameEt
                    }
                    viewDataBinding.registerConfirmPasswordEt.isFocused -> {
                        viewDataBinding.registerNameEt
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