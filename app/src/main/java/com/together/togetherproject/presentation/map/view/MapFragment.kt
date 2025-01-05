package com.together.togetherproject.presentation.map.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kakao.vectormap.*
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.together.togetherproject.R
import com.together.togetherproject.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentMapBinding? = null

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    private var startPosition: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMapView()
    }

    private fun showMapView() {
        mapView = binding.mapView

        // KakaoMapSDK 초기화
        KakaoMapSdk.init(requireContext(), getString(R.string.kakao_map_key))

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "onMapDestroy")
                }

                override fun onMapError(exception: Exception?) {
                    Log.e("KakaoMap", "onMapError", exception)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMapInstance: KakaoMap) {
                    kakaoMap = kakaoMapInstance
                    Log.d("KakaoMap", "Map is ready")

                    val labelManager = kakaoMap?.labelManager
                    val layer = labelManager?.getLayer()
                    val trackingManager = kakaoMap?.trackingManager

                    if (labelManager != null && layer != null && trackingManager != null) {
                        val centerLabel = layer.addLabel(
                            LabelOptions.from("centerLabel", startPosition)
                                .setStyles(LabelStyle.from(R.drawable.red_dot_marker).setAnchorPoint(0.5f, 0.5f))
                                .setRank(1)
                        )
                        trackingManager.startTracking(centerLabel)
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}