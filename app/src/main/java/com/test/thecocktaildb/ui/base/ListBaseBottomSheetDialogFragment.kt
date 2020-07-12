package com.test.thecocktaildb.ui.base

import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import android.widget.FrameLayout
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType
import kotlinx.android.synthetic.main.layout_dialog_list_component.*

abstract class ListBaseBottomSheetDialogFragment<
        Data,
        ButtonType : DialogButton,
        Type : DialogType<ButtonType>>
protected constructor() :
    SimpleBaseBottomSheetDialogFragment<Data, ButtonType, Type, SimpleBaseBottomSheetDialogFragment.SimpleBottomSheetDialogBuilder>() {

    override val contentLayoutResId = R.layout.layout_dialog_simple
    override val extraContentLayoutResId: Int = R.layout.layout_dialog_list_component

    abstract val dialogListDataAdapter: DialogListDataAdapter<Data>
    protected open val listAdapter: BaseDialogAdapter<Data, *> = DialogListAdapter()

    open var listData: List<Data> = mutableListOf()

    override fun configureExtraContent(container: FrameLayout, savedInstanceState: Bundle?) {
        super.configureExtraContent(container, savedInstanceState)
        listAdapter.setNewData(listData)
        rv_dialog_list.apply {
            setHasFixedSize(true)
            adapter = this@ListBaseBottomSheetDialogFragment.listAdapter
        }
    }

    protected open inner class DialogListAdapter :
        BaseDialogAdapter<Data, BaseDialogViewHolder>(R.layout.item_dialog_list),
        View.OnClickListener {

        override fun convert(helper: BaseDialogViewHolder, position: Int) {
            with(helper.itemView as CheckedTextView) {
                text = dialogListDataAdapter.getName(listData[position])
                tag = listData[position]
                setOnClickListener(this@DialogListAdapter)
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun onClick(v: View?) {
            /**
             * be sure to override method [obtainDataForView] and handle your model [Data]
             */
            callOnClick(v ?: return, getButtonType(v))
        }
    }
}

interface DialogListDataAdapter<Data> {
    fun getName(data: Data): CharSequence
}
