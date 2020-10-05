package com.test.seekbar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentRangeSeekBarBinding
import com.test.thecocktaildb.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.presentation.ui.custom.SeekBarView

class RangeSeekBarFragment : BaseFragment<FragmentRangeSeekBarBinding>(), SeekBarView.SeekBarChangeListener {

    companion object {

        @JvmStatic
        fun newInstance(): RangeSeekBarFragment {
            return RangeSeekBarFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_range_seek_bar

    override val viewModel: RangeSeekBarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding.seekBarView.seekBarChangeListener = this

        viewDataBinding.seekBarViewLeftIndicator.setText(viewDataBinding.seekBarView.min.toString())
        viewDataBinding.seekBarViewRightIndicator.setText(viewDataBinding.seekBarView.max.toString())
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.fragment = this
    }

    fun setRange() {
        viewDataBinding.seekBarView.setRange(20, 50, true)
    }

    override fun onStartedSeeking() {
    }

    override fun onStoppedSeeking() {
    }

    override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int) {
        viewDataBinding.seekBarViewLeftIndicator.setText(minThumbValue.toString())
        viewDataBinding.seekBarViewRightIndicator.setText(maxThumbValue.toString())
    }
}