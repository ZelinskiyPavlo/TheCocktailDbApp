package com.test.thecocktaildb.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDialogAdapter<Data, BaseViewHolder : BaseDialogViewHolder>(
    private val contentLayoutRestId: Int
) : RecyclerView.Adapter<BaseDialogViewHolder>() {

    lateinit var items: List<Data>

    fun setNewData(data: List<Data>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDialogViewHolder {
        return BaseDialogViewHolder(
            (LayoutInflater.from(parent.context)
                .inflate(contentLayoutRestId, parent, false))
        )
    }

    override fun onBindViewHolder(holder: BaseDialogViewHolder, position: Int) {
        convert(holder, position)
    }

    abstract fun convert(helper: BaseDialogViewHolder, position: Int)

    override fun getItemCount(): Int {
        return items.size
    }
}

open class BaseDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
