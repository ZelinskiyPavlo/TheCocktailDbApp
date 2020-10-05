package com.test.presentation.exception

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.test.common.exception.*
import com.test.presentation.R
import com.test.presentation.ui.dialog.RegularDialogFragment

class SimpleErrorHandler(
    private val fragmentManager: FragmentManager,
    private val context: Context
) {

    fun handleLoginError(e: LoginError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.login_error_dialog_title
            descriptionTextResId = R.string.login_error_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, LoginError::class.java.name)
    }

    fun handleRegistrationError(e: RegistrationError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.register_error_dialog_title
            descriptionTextResId = R.string.register_error_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, RegistrationError::class.java.name)
    }

    fun handleApiError(e: ApiError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.api_error_dialog_title
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, ApiError::class.java.name)
    }

    fun handleUnAuthorizedAccessError(e: UnAuthorizedAccessError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.unauthorized_access_error_dialog_title
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, UnAuthorizedAccessError::class.java.name)
    }

    fun handleServerError(e: ServerError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.server_error_dialog_title
            descriptionTextResId = R.string.server_error_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleServerRespondingError(e: ServerRespondingError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.server_responding_error_dialog_title
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleUnknownError(e: UnknownError) {
        val withCode = context.getString(R.string.unknown_error_dialog_description_with_code)
        val andDetails = context.getString(R.string.unknown_error_dialog_description_and_details)
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.unknown_error_dialog_title
            descriptionText = "$withCode ${e.code} $andDetails ${e.details}"
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, UnknownError::class.java.name)
    }

    fun handleCancellationError(e: CancellationError) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.cancellation_error_dialog_title
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, ServerError::class.java.name)
    }

    fun handleNoInternetConnectionError(e: NoInternetConnectionError) {
        if (checkIfDialogCurrentlyVisible()) return

        RegularDialogFragment.newInstance {
            titleTextResId = R.string.no_internet_error_dialog_title
            descriptionTextResId = R.string.no_internet_error_dialog_description
            rightButtonTextResId = R.string.all_ok
        }.show(fragmentManager, NoInternetConnectionError::class.java.name)
    }

    private fun checkIfDialogCurrentlyVisible(): Boolean {
        fragmentManager.fragments.lastOrNull()?.let {
            if (it::class.java.name == RegularDialogFragment::class.java.name) return true
        }
        return false
    }
}