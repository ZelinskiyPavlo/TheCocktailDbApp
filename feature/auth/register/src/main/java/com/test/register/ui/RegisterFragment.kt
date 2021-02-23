package com.test.register.ui

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.test.navigation.HasBackPressLogic
import com.test.presentation.extension.addLinkedText
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.dialog.RegularDialogFragment
import com.test.presentation.util.EventObserver
import com.test.register.R
import com.test.register.api.RegisterNavigationApi
import com.test.register.databinding.FragmentRegisterBinding
import com.test.register.factory.RegisterViewModelFactory
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
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    override val viewModel: RegisterViewModel by viewModels {
        SavedStateViewModelFactory(registerViewModelFactory, this)
    }

//    private val sharedViewModel: AuthViewModel by activityViewModels()

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
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setWhiteSpaceFilter()
        addLinkedText()
        setupObserver()
        configureTogglePasswordButton()

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
            .addLinkedText(this.getString(R.string.all_sign_in)) { navigateToLoginFragment() }
    }

    private fun setupObserver() {
        viewModel.registerEventLiveData.observe(
            viewLifecycleOwner, EventObserver {
                navigateToCocktailActivity()
            })

        viewModel.clearErrorTextColorEventLiveData.observe(
            viewLifecycleOwner, EventObserver {
                clearErrors()
            }
        )

        viewModel.isDataValidLiveData.observe(viewLifecycleOwner, { isAvailable ->
            viewDataBinding.registerButtonRegister.isEnabled = isAvailable
        })
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

    private fun navigateToLoginFragment() {
        registerNavigator.toLogin()
    }

    private fun navigateToCocktailActivity() {
//        val (notificationType, cocktailId) = sharedViewModel.firebaseData
        registerNavigator.toTabHost()
    }

    override fun onBackPressed() {
        registerNavigator.toLogin()
    }

    private fun isTypedDataContainErrors(): Boolean {
        if (viewModel.isDataValidLiveData.value == null) return true
        var error = false

        fun markFieldAsError(textField: TextInputEditText) {
            textField.setTextColor(errorTextColor)
            textField.requestFocus()
            error = true
        }

        with(viewModel) {
            if (passwordInputLiveData.value != confirmPasswordInputLiveData.value) {
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
        fun clearFieldError(textField: TextInputEditText) {
            textField.setTextColor(regularTextColor)
        }

        viewDataBinding.registerConfirmPasswordEtl.error = null

        clearFieldError(viewDataBinding.registerEmailEt)
        clearFieldError(viewDataBinding.registerNameEt)
        clearFieldError(viewDataBinding.registerLastNameEt)
        clearFieldError(viewDataBinding.registerPasswordEt)
        clearFieldError(viewDataBinding.registerConfirmPasswordEt)
    }

    private fun showInvalidInputDialog() {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.invalid_register_input_dialog_title
            descriptionTextResId = R.string.invalid_register_input_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(childFragmentManager, "SignUpErrorDialog")
    }

}