package com.test.presentation.adapter.recyclerview.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.test.presentation.adapter.recyclerview.CustomActionListener

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewDataBinding: ViewDataBinding = DataBindingUtil.inflate(
            layoutInflater, viewType, parent, false
        )
        return BaseViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item: T = getItemForPosition(position)
        val userActionListener = getItemClickListener()
        holder.bind(item, userActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    abstract fun setData(items: List<T>?)

    protected abstract fun getItemForPosition(position: Int): T

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    protected abstract fun getItemClickListener(): CustomActionListener

    inner class BaseViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun <T> bind(item: T, listener: CustomActionListener) {
            binding.setVariable(BR.obj, item)
            binding.setVariable(BR.listener, listener)

            binding.executePendingBindings()
        }
    }
}