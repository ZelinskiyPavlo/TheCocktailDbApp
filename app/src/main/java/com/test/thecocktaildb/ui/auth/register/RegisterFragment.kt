package com.test.thecocktaildb.ui.auth.register

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
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentRegisterBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentationNew.extension.addLinkedText
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.dialog.RegularDialogFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.RegisterViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import javax.inject.Inject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(), Injectable {

    override val layoutId: Int = R.layout.fragment_register

    @Inject
    lateinit var registerViewModelFactory: RegisterViewModelFactory

    override val viewModel: RegisterViewModel by viewModels {
        SavedStateViewModelFactory(registerViewModelFactory, this)
    }

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
            .addLinkedText("Sign in") { navigateToLoginFragment() }
    }

    private fun setupObserver() {
        viewModel.registerEventLiveData.observe(
            viewLifecycleOwner, EventObserver { isRegisterSuccessful ->
                if (isRegisterSuccessful) navigateToCocktailActivity()
                else showErrorDialog()
            })
        viewModel.clearErrorTextColorEventLiveData.observe(
            viewLifecycleOwner, EventObserver {
                clearErrors()
            }
        )
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

    private fun navigateToLoginFragment() {
        findNavController().popBackStack()
    }

    private fun navigateToCocktailActivity() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
        findNavController().navigate(action)
        activity?.finish()
    }

    fun onRegisterButtonClicked() {
        if (isTypedDataContainErrors()) return

        val view = activity?.currentFocus
        view?.let { v ->
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
        viewModel.registerUser()
    }

    private fun isTypedDataContainErrors(): Boolean {
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

    private fun showErrorDialog() {
        RegularDialogFragment.newInstance {
            titleText = "Sign up error"
            rightButtonText = "Ok"
            descriptionText = "Looks like you provided wrong login or password"
        }.show(childFragmentManager, "SignInErrorDialog")
    }

}