package com.test.thecocktaildb.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.di.Injectable
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel> : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewDataBinding: VDB
    protected lateinit var viewModel: VM

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        setupDataBinding(inflater, container)

        return viewDataBinding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return
    }
}