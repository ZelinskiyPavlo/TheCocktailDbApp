package com.test.thecocktaildb.ui.base

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import com.test.thecocktaildb.core.common.exception.RequestError
import com.test.thecocktaildb.presentationNew.extension.observeNotNull
import com.test.thecocktaildb.presentationNew.extension.observeNotNullOnce
import com.test.thecocktaildb.presentationNew.extension.observeTillDestroy
import com.test.thecocktaildb.presentationNew.extension.observeTillDestroyNotNull
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType
import com.test.thecocktaildb.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.ui.dialog.base.BaseDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import icepick.Icepick
import timber.log.Timber
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, contentLayoutResId) as VDB
        dataBinding.lifecycleOwner = this@BaseActivity
        configureDataBinding()

        Icepick.restoreInstanceState(this, savedInstanceState)
    }

    protected open fun configureDataBinding() {
        //stub
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        addViewModelErrorObserver(viewModel)
    }

    protected fun addViewModelErrorObserver(viewModel: BaseViewModel) {
        viewModel.errorLiveData.observeNotNull {
            handleError(it)
        }
    }

    protected open fun handleError(e: RequestError) {

    }

    protected open fun configureView(savedInstanceState: Bundle?) {
        //stub
    }

    protected open fun configureObserver() {
        //stub
    }

    override fun onStop() {
        super.onStop()

        Timber.i("OnStop called ${this@BaseActivity.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.i("OnDestroy called ${this@BaseActivity.javaClass.simpleName}")
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