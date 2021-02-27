package com.test.presentation.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.common.exception.*
import com.test.presentation.exception.SimpleErrorHandler
import com.test.presentation.extension.observeNotNull
import com.test.presentation.ui.dialog.DialogButton
import com.test.presentation.ui.dialog.DialogType
import com.test.presentation.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.presentation.ui.dialog.base.BaseDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import icepick.Icepick
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding> : Fragment(), HasAndroidInjector,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>> {

    protected open lateinit var viewDataBinding: VDB

    abstract val layoutId: Int

    open val viewModel: BaseViewModel by viewModels()

    private val errorHandler: SimpleErrorHandler
            by lazy { SimpleErrorHandler(childFragmentManager, requireContext()) }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        configureDataBinding()

        Icepick.restoreInstanceState(this, savedInstanceState)
        return viewDataBinding.root
    }

    protected open fun configureDataBinding() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addViewModelErrorObserver(viewModel)
    }

    protected fun addViewModelErrorObserver(viewModel: BaseViewModel) {
        viewModel.errorLiveData.observeNotNull(viewLifecycleOwner, {
            handleError(it)
        })
    }

    open fun onBackPressed() {}

    private fun handleError(e: RequestError) {
        when (e) {
            is LoginError -> handleLoginError(e)
            is RegistrationError -> handleRegistrationError(e)
            is ApiError -> handleApiError(e)
            is UnAuthorizedAccessError -> handleUnAuthorizedAccessError(e)
            is ServerError -> handleServerError(e)
            is ServerRespondingError -> handleServerRespondingError(e)
            is UnknownError -> handleUnknownError(e)
            is CancellationError -> handleCancellationError(e)
            is NoInternetConnectionError -> handleNoInternetConnectionError(e)
        }
    }

    protected open fun handleLoginError(e: LoginError) =
        errorHandler.handleLoginError()
    protected open fun handleRegistrationError(e: RegistrationError) =
        errorHandler.handleRegistrationError()
    protected open fun handleApiError(e: ApiError) =
        errorHandler.handleApiError()
    protected open fun handleUnAuthorizedAccessError(e: UnAuthorizedAccessError) =
        errorHandler.handleUnAuthorizedAccessError()
    protected open fun handleServerError(e: ServerError) =
        errorHandler.handleServerError()
    protected open fun handleServerRespondingError(e: ServerRespondingError) =
        errorHandler.handleServerRespondingError()
    protected open fun handleUnknownError(e: UnknownError) =
        errorHandler.handleUnknownError(e)
    protected open fun handleCancellationError(e: CancellationError) =
        errorHandler.handleCancellationError()
    protected open fun handleNoInternetConnectionError(e: NoInternetConnectionError) =
        errorHandler.handleNoInternetConnectionError()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        data: Any?
    ) {
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        buttonType: DialogButton,
        data: Any?
    ) {
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
    }
}