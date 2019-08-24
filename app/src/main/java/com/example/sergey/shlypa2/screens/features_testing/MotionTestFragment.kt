package com.example.sergey.shlypa2.screens.features_testing


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sergey.shlypa2.R
import com.example.sergey.shlypa2.ads.AdsManager
import kotlinx.android.synthetic.main.fragment_testing.*
import org.koin.android.ext.android.inject


class MotionTestFragment : Fragment() {

    private val adsManager by inject<AdsManager>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_testing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adsManager.getNativeAd(requireContext()) {
            my_template.setNativeAd(it)
        }
    }
}
