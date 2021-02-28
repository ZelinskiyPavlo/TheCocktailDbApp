package com.test.cube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.cube.databinding.FragmentCubeBinding
import com.test.presentation.ui.base.BaseFragment

class CubeFragment : BaseFragment<FragmentCubeBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): CubeFragment {
            return CubeFragment()
        }
    }

    override val layoutId: Int = com.test.cube.R.layout.fragment_cube

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