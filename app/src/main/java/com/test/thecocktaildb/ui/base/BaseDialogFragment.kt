package com.test.thecocktaildb.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType

abstract class BaseDialogFragment<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> protected
constructor() :
    DialogFragment(),
    View.OnClickListener {

    protected var onDialogClickListener: OnDialogFragmentClickListener<Data, ButtonType, Type>? =
        null
        private set
    protected var onDialogDismissListener: OnDialogFragmentDismissListener<Data, ButtonType, Type>? =
        null
        private set

    protected abstract val dialogType: Type
    protected open var data: Data? = null
    protected abstract val contentLayoutResId: Int

    private val clickableViews = mutableListOf<View>()

    init {
        this.setStyle(STYLE_NO_TITLE, R.style.DialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentLayoutResId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickableViews.clear()
        clickableViews.addAll(obtainClickableViews())
        clickableViews.forEach {
            it.setOnClickListener(this)
        }
    }

    protected open fun obtainClickableViews(): List<View> {
        return emptyList()
    }

    protected abstract fun getButtonType(view: View): ButtonType

    @Suppress("UNUSED_PARAMETER")
    protected open fun obtainDataForView(view: View, buttonType: ButtonType): Data? {
        return data
    }

    override fun onClick(v: View?) {
        val buttonType = getButtonType(v ?: return)
        callOnClick(v, buttonType)
    }

    protected open fun callOnClick(v: View, buttonType: ButtonType) {
        onDialogClickListener?.apply {
            val data = obtainDataForView(v, buttonType)
            val acceptClick = this.shouldDialogFragmentAcceptClick(
                this@BaseDialogFragment,
                dialogType,
                buttonType,
                data
            )

            if (!acceptClick) return

            this.onDialogFragmentClick(
                this@BaseDialogFragment,
                dialogType,
                buttonType,
                data
            )
        }
        dismiss()
    }

    @Suppress("UNUSED_PARAMETER")
    protected open fun configureDialog(dialog: Dialog) {
        //stub
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.also {
            it.attributes.windowAnimations = R.style.DialogFragment
        }
        dialog.setOnDismissListener(this)
        configureDialog(dialog)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismissListener?.onDialogFragmentDismiss(
            this,
            dialogType,
            data
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is OnDialogFragmentClickListener<*, *, *>) {
            "Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnDialogFragmentClickListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogClickListener =
            context as? OnDialogFragmentClickListener<Data, ButtonType, Type>
        check(context is OnDialogFragmentDismissListener<*, *, *>) {
            "Dialog must be attached to context " +
                    "(activity/fragment) that implements ${OnDialogFragmentDismissListener::class.java.simpleName} " +
                    "listener"
        }
        onDialogDismissListener =
            context as? OnDialogFragmentDismissListener<Data, ButtonType, Type>
    }

    override fun onDetach() {
        super.onDetach()
        onDialogClickListener = null
        onDialogDismissListener = null
    }

    interface OnDialogFragmentDismissListener<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> {
        fun onDialogFragmentDismiss(
            dialog: DialogFragment,
            dialogType: Type,
            data: Data?
        )
    }

    interface OnDialogFragmentClickListener<Data, ButtonType : DialogButton, Type : DialogType<ButtonType>> {
        fun onDialogFragmentClick(
            dialog: DialogFragment,
            dialogType: Type,
            buttonType: ButtonType,
            data: Data?
        )

        fun shouldDialogFragmentAcceptClick(
            dialog: DialogFragment,
            dialogType: Type,
            buttonType: ButtonType,
            data: Data?
        ): Boolean {
            return true
        }
    }
}