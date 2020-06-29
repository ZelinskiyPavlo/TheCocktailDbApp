package com.test.thecocktaildb.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(),
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("__________in onCreate method ")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("__________in onStart method ")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("__________in onResume method ")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("__________in onPause method ")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("__________in onStop method ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("__________in onDestroy method ")
    }

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)
            ?.onDialogFragmentDismiss(dialog, dialogType, data)
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        buttonType: DialogButton,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)
            ?.onDialogFragmentClick(dialog, dialogType, buttonType, data)
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)
            ?.onBottomSheetDialogFragmentDismiss(dialog, type, data)
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        (dialog.parentFragment as? BaseFragment<*, *>)
            ?.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
    }
}