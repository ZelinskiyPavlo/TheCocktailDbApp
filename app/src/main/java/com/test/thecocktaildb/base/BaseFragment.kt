package com.test.thecocktaildb.base

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

    protected lateinit var mViewDataBinding: VDB
    protected lateinit var mViewModel: VM

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

        return mViewDataBinding.root
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mViewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return
    }
}