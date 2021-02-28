package com.test.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.test.presentation.R
import com.test.presentation.ui.dialog.base.SimpleBaseBottomSheetDialogFragment

class RegularBottomSheetDialogFragment :
    SimpleBaseBottomSheetDialogFragment<Any, RegularDialogButton, RegularDialogType,
            SimpleBaseBottomSheetDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val dialogType: RegularDialogType = RegularDialogType

    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()
    override var data: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
    }

    override fun obtainDataForView(view: View): Any? {
        return data
    }

    override fun getButtonType(view: View): RegularDialogButton {
        return when (view.id) {
            R.id.lb_dialog_bs_left -> LeftDialogButton
            R.id.lb_dialog_bs_right -> RightDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    companion object {
        fun newInstance(builder: SimpleBottomSheetDialogBuilder.() -> Unit): RegularBottomSheetDialogFragment {
            return getInstance(builder)
        }

        /**
         * Note trick that data can be used as tag to distinguish dialogs
         * if multiple instances exists within one context (activity/fragment)
         */
        fun getInstance(
            builder: SimpleBottomSheetDialogBuilder.() -> Unit
        ): RegularBottomSheetDialogFragment {
            val fragment = RegularBottomSheetDialogFragment()
            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to (SimpleBottomSheetDialogBuilder().apply(builder))
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}