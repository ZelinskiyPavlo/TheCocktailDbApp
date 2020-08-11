package com.test.thecocktaildb.ui.dialog

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.text.bold
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.dialog.base.BaseDialogViewHolder
import com.test.thecocktaildb.ui.dialog.base.DialogListDataAdapter
import com.test.thecocktaildb.ui.dialog.base.ListBaseBottomSheetDialogFragment
import com.test.thecocktaildb.util.locale.LanguageType

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
            R.id.dialog_list_item -> ItemListDialogButton
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
            with(helper.itemView as TextView) {
                text = if (listData[position] != selectedLanguage)
                    dialogListDataAdapter.getName(listData[position])
                else markTextSelected(dialogListDataAdapter.getName(listData[position]).toString())
                tag = listData[position]
                setOnClickListener(this@LanguageListAdapter)
            }
        }

        private fun markTextSelected(text: String): SpannableStringBuilder {
            return SpannableStringBuilder().bold { append(text) }
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