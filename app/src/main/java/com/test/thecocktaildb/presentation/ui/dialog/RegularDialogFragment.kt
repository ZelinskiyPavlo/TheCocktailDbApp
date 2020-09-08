package com.test.thecocktaildb.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.test.thecocktaildb.R
import com.test.thecocktaildb.presentation.ui.dialog.base.SimpleBaseDialogFragment

class RegularDialogFragment :
    SimpleBaseDialogFragment<Any, RegularDialogButton, RegularDialogType, SimpleBaseDialogFragment
    .SimpleBaseDialogFragmentBuilder>() {

    override val dialogType: RegularDialogType = RegularDialogType

    override var dialogBuilder: SimpleBaseDialogFragment.SimpleBaseDialogFragmentBuilder =
        SimpleBaseDialogFragment.SimpleBaseDialogFragmentBuilder()
    override var data: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

//    override fun obtainDataForView(view: View, buttonType: ButtonType): Any? {
//        return data
//    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.lb_dialog_bs_left -> LeftDialogButton
            R.id.lb_dialog_bs_right -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    companion object {
        fun newInstance(builder: SimpleBaseDialogFragment.SimpleBaseDialogFragmentBuilder.() ->
        Unit): RegularDialogFragment {
            return getInstance(builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleBaseDialogFragment.SimpleBaseDialogFragmentBuilder.() -> Unit
        ): RegularDialogFragment {
            val fragment = RegularDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleBaseDialogFragment.SimpleBaseDialogFragmentBuilder()
                    .apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}