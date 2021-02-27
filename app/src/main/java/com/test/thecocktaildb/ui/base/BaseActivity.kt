package com.test.thecocktaildb.ui.base

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.test.common.exception.*
import com.test.presentation.exception.SimpleErrorHandler
import com.test.presentation.extension.observeNotNull
import com.test.presentation.extension.observeNotNullOnce
import com.test.presentation.extension.observeTillDestroy
import com.test.presentation.extension.observeTillDestroyNotNull
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.ui.base.BaseViewModel
import com.test.presentation.ui.dialog.DialogButton
import com.test.presentation.ui.dialog.DialogType
import com.test.presentation.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.presentation.ui.dialog.base.BaseDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import icepick.Icepick
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    HasAndroidInjector,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>> {

    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any>

    protected open lateinit var dataBinding: VDB

    abstract val contentLayoutResId: Int

    abstract val viewModel: VM

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private val errorHandler: SimpleErrorHandler
            by lazy { SimpleErrorHandler(supportFragmentManager, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, contentLayoutResId) as VDB
        dataBinding.lifecycleOwner = this@BaseActivity
        configureDataBinding()

        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    protected open fun configureDataBinding() {
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        configureObserver()
        addViewModelErrorObserver(viewModel)
    }

    protected fun addViewModelErrorObserver(viewModel: BaseViewModel) {
        viewModel.errorLiveData.observeNotNull {
            handleError(it)
        }
    }

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
        errorHandler.handleLoginError(e)
    protected open fun handleRegistrationError(e: RegistrationError) =
        errorHandler.handleRegistrationError(e)
    protected open fun handleApiError(e: ApiError) =
        errorHandler.handleApiError(e)
    protected open fun handleUnAuthorizedAccessError(e: UnAuthorizedAccessError) =
        errorHandler.handleUnAuthorizedAccessError(e)
    protected open fun handleServerError(e: ServerError) =
        errorHandler.handleServerError(e)
    protected open fun handleServerRespondingError(e: ServerRespondingError) =
        errorHandler.handleServerRespondingError(e)
    protected open fun handleUnknownError(e: UnknownError) =
        errorHandler.handleUnknownError(e)
    protected open fun handleCancellationError(e: CancellationError) =
        errorHandler.handleCancellationError(e)
    protected open fun handleNoInternetConnectionError(e: NoInternetConnectionError) =
        errorHandler.handleNoInternetConnectionError(e)

    protected open fun configureObserver() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

    //region Convenient Observe Methods
    @MainThread
    protected inline fun <reified T> LiveData<T>.observe(noinline observer: (T) -> Unit) {
        this.observe(this@BaseActivity, observer)
    }

    @MainThread
    protected fun <T> LiveData<T?>.observeNotNull(observer: (T) -> Unit) {
        this.observeNotNull(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T?>.observeTillDestroyNotNull(crossinline observer: (T) -> Unit) {
        this.observeTillDestroyNotNull(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T>.observeTillDestroy(crossinline observer: (T) -> Unit) {
        this.observeTillDestroy(this@BaseActivity, observer)
    }

    @MainThread
    protected inline fun <T> LiveData<T>.observeNonNullOnce(crossinline observer: (T) -> Unit) {
        this.observeNotNullOnce(this@BaseActivity, observer)
    }
    //endregion

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*>)
            ?.onDialogFragmentDismiss(dialog, dialogType, data)
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        buttonType: DialogButton,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*>)
            ?.onDialogFragmentClick(dialog, dialogType, buttonType, data)
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*>)
            ?.onBottomSheetDialogFragmentDismiss(dialog, type, data)
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*>)
            ?.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
    }
}