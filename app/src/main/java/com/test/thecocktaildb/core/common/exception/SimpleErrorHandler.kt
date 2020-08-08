package com.test.thecocktaildb.core.common.exception

import androidx.fragment.app.FragmentManager
import com.test.thecocktaildb.ui.dialog.RegularDialogFragment

class SimpleErrorHandler(private val fragmentManager: FragmentManager) {

    fun handleLoginError(e: LoginError) {
        RegularDialogFragment.newInstance {
            titleText = "Login error"
            descriptionText = "Cannot login user with specified login and password"
            rightButtonText = "Ok"
        }.show(fragmentManager, LoginError::class.java.name)
    }

    fun handleRegistrationError(e: RegistrationError) {
        RegularDialogFragment.newInstance {
            titleText = "Registration error"
            descriptionText = "This user already exist"
            rightButtonText = "Ok"
        }.show(fragmentManager, RegistrationError::class.java.name)
    }

    fun handleApiError(e: ApiError) {
        RegularDialogFragment.newInstance {
            titleText = "Api error"
            rightButtonText = "Ok"
        }.show(fragmentManager, ApiError::class.java.name)
    }

    fun handleUnAuthorizedAccessError(e: UnAuthorizedAccessError) {
        RegularDialogFragment.newInstance {
            titleText = "Unauthorized access error"
            rightButtonText = "Ok"
        }.show(fragmentManager, UnAuthorizedAccessError::class.java.name)
    }

    fun handleServerError(e: ServerError) {
        RegularDialogFragment.newInstance {
            titleText = "Server error"
            descriptionText = "Some problem occurred with server"
            rightButtonText = "Ok"
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleServerRespondingError(e: ServerRespondingError) {
        RegularDialogFragment.newInstance {
            titleText = "Server not responding"
            rightButtonText = "Ok"
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleUnknownError(e: UnknownError) {
        RegularDialogFragment.newInstance {
            titleText = "Unknown error"
            descriptionText = "With code ${e.code} and details ${e.details}"
            rightButtonText = "Ok"
        }.show(fragmentManager, UnknownError::class.java.name)
    }

    fun handleCancellationError(e: CancellationError) {
        RegularDialogFragment.newInstance {
            titleText = "Some cancellation error"
            rightButtonText = "Ok"
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleNoInternetConnectionError(e: NoInternetConnectionError) {
        if(checkIfDialogCurrentlyVisible()) return

        RegularDialogFragment.newInstance {
            titleText = "You don't have access to the internet"
            descriptionText = "Please check your internet connection"
            rightButtonText = "Ok"
        }.show(fragmentManager, NoInternetConnectionError::class.java.name)
    }

    private fun checkIfDialogCurrentlyVisible(): Boolean {
        fragmentManager.fragments.lastOrNull()?.let {
            if(it::class.java.name == RegularDialogFragment::class.java.name) return true
        }
        return false
    }
}