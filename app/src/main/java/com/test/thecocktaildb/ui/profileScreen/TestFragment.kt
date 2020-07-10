package com.test.thecocktaildb.ui.profileScreen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.test.thecocktaildb.R
import timber.log.Timber
import java.util.*

const val TEST_NUMBER = "test number"
const val TEST_STRING = "test string"

class TestFragment : Fragment() {

    private var testNumber: Int? = null
    private var optionalString: String? = null

    private lateinit var testTextView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach method of TestFragment Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate method of TestFragment Called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        Timber.d("onCreateView method of TestFragment Called")

        testTextView = view.findViewById(R.id.tv_test)
        testNumber = requireArguments().getInt(TEST_NUMBER)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated method of TestFragment Called")

        setBackgroundColor()
        addListenerToTextView()
    }

    private fun setBackgroundColor() {
        if (requireArguments().containsKey(TEST_STRING)) {
            optionalString = requireArguments().getString(TEST_STRING)
            requireView().setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.app_background
                )
            )

            val finalString = "$testNumber $optionalString"
            testTextView.text = finalString

        } else {
            val random = Random()
            val backgroundColor: Int =
                Color.argb(
                    255, random.nextInt(256), random.nextInt(256),
                    random.nextInt(256)
                )
            requireView().setBackgroundColor(backgroundColor)

            testTextView.text = "$testNumber"
        }
    }

    private fun addListenerToTextView() {
        testTextView.setOnClickListener {

            val testFragment1 = newInstance(testNumber!!.inc())

            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container, testFragment1)
                .addToBackStack("transaction_1")
                .apply { commit() }

            val testFragment2 = newInstance(testNumber!!.inc())

            val testFragment3 = newInstance(testNumber!!.inc())

            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container, testFragment2)
                .add(R.id.profile_fragment_container, testFragment3)
                .addToBackStack("transaction_2")
                .apply { commit() }

            val testFragment4 = newInstance(testNumber!!.inc())
            val testFragment5 = newInstance(testNumber!!, TEST_STRING)

            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.profile_fragment_container, testFragment4)
                .add(R.id.profile_fragment_container, testFragment5)
                .apply { commit() }

            if (optionalString != null) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .remove(this)
                    .addToBackStack("transaction_4")
                    .apply { commit() }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart method of TestFragment Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume method of TestFragment Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause method of TestFragment Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop method of TestFragment Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView method of TestFragment Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy method of TestFragment Called")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach method of TestFragment Called")
    }

    companion object {
        @JvmStatic
        fun newInstance(testNumber: Int, testString: String? = null) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putInt(TEST_NUMBER, testNumber)
                    if (testString != null) {
                        putString(TEST_STRING, testString)
                    }
                }
            }
    }
}