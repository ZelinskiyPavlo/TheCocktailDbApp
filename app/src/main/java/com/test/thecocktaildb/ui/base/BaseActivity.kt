package com.test.thecocktaildb.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.test.thecocktaildb.presentationNew.extension.observeNotNull
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import icepick.Icepick
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding, VM: BaseViewModel> : AppCompatActivity(),
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        viewModel.errorLiveData.observeNotNull (this@BaseActivity) {
            //TODO handle error
            Toast.makeText(this, "error = ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()

        Timber.i("OnStop called ${this@BaseActivity.javaClass.simpleName}")
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.i("OnDestroy called ${this@BaseActivity.javaClass.simpleName}")
    }

    protected open fun configureDataBinding() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Icepick.saveInstanceState(this, outState)
    }

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