package com.test.thecocktaildb.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.dialog.DialogButton
import com.test.thecocktaildb.ui.dialog.DialogType
import com.test.thecocktaildb.ui.dialog.base.BaseBottomSheetDialogFragment
import com.test.thecocktaildb.ui.dialog.base.BaseDialogFragment
import com.test.thecocktaildb.util.vmfactory.DelegatedViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel> : Fragment(), Injectable,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseDialogFragment.OnDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>,
    BaseBottomSheetDialogFragment.OnBottomSheetDialogFragmentDismissListener<Any, DialogButton, DialogType<DialogButton>> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var delegatedViewModelFactory: DelegatedViewModelFactory

    protected lateinit var viewDataBinding: VDB
    protected lateinit var viewModel: VM

    abstract val layoutId: Int

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureView(savedInstanceState)
    }

    protected open fun configureView(savedInstanceState: Bundle?) {
        // stub
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[getViewModelClass()]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return
    }

    override fun onDialogFragmentDismiss(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        data: Any?
    ) {
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        buttonType: DialogButton,
        data: Any?
    ) {
    }

    override fun onBottomSheetDialogFragmentDismiss(
        dialog: DialogFragment,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {

    }
}