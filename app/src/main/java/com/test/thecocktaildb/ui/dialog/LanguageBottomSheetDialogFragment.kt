package com.test.thecocktaildb.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import androidx.core.os.bundleOf
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseDialogViewHolder
import com.test.thecocktaildb.ui.base.DialogListDataAdapter
import com.test.thecocktaildb.ui.base.ListBaseBottomSheetDialogFragment
import com.test.thecocktaildb.util.LanguageType

open class LanguageListBottomSheetDialogFragment :
    ListBaseBottomSheetDialogFragment<LanguageType?, ListDialogButton, LanguageDialogType>() {

    override val dialogType: LanguageDialogType = LanguageDialogType

    override val dialogListDataAdapter: DialogListDataAdapter<LanguageType?> = object :
        DialogListDataAdapter<LanguageType?> {
        override fun getName(data: LanguageType?): CharSequence {
            return data?.language ?: ""
        }
    }
    override val listAdapter = LanguageListAdapter()

    private lateinit var selectedLanguage: LanguageType

    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        selectedLanguage =
            LanguageType.values()[requireArguments().getInt(EXTRA_KEY_SELECTED_LANGUAGE)]
    }

    override var listData: List<LanguageType?> = LanguageType.values().sortedBy { it.ordinal }

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.btn_item_dialog -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }

    override fun obtainDataForView(view: View): LanguageType {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as LanguageType
            else -> throw NotImplementedError("not implemented buttonType (${getButtonType(view)}) for dialog ${this::class.java.simpleName}")
        }
    }

    protected inner class LanguageListAdapter : DialogListAdapter(), View.OnClickListener {

        override fun convert(helper: BaseDialogViewHolder, position: Int) {
            with(helper.itemView as CheckedTextView) {
                text = dialogListDataAdapter.getName(listData[position])
                tag = listData[position]
                isChecked = (listData[position] == selectedLanguage)
                setOnClickListener(this@LanguageListAdapter)
            }
        }
    }

    companion object {
        fun newInstance(selectedLanguageIndex: Int): LanguageListBottomSheetDialogFragment {
            val fragment = LanguageListBottomSheetDialogFragment()

            fragment.arguments = bundleOf(
                EXTRA_KEY_BUILDER to SimpleBottomSheetDialogBuilder()
                    .apply {
                        titleTextResId = R.string.dialog_language_title
                        isCancelable = true
                        isCloseButtonVisible = true
                    },
                EXTRA_KEY_SELECTED_LANGUAGE to selectedLanguageIndex
            )
            return fragment
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_LANGUAGE = "EXTRA_KEY_SELECTED_LANGUAGE"
    }

}