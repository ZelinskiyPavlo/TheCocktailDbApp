package com.test.thecocktaildb.presentation.ui.setting.cube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentCubeBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentation.ui.base.BaseFragment

class CubeFragment : Injectable, BaseFragment<FragmentCubeBinding>() {

    companion object {

        @JvmStatic
        fun newInstance(): CubeFragment {
            return CubeFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_cube

    override val viewModel: CubeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        viewDataBinding.cubeFragmentView.setOnClickListener {
            viewDataBinding.cubeFragmentView.handleAnimation()
        }

        return viewDataBinding.root
    }

}