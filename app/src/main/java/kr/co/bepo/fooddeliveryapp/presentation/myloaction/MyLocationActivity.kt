package kr.co.bepo.fooddeliveryapp.presentation.myloaction

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.databinding.ActivityMyLocationBinding
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseActivity
import kr.co.bepo.fooddeliveryapp.presentation.home.HomeViewModel
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    MapView.MapViewEventListener {

    companion object {
        const val CAMERA_ZOOM_LEVEL = 0

        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }
    }

    override val viewModel: MyLocationViewModel by viewModel() {
        parametersOf(
            intent.getParcelableExtra(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding(): ActivityMyLocationBinding =
        ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var kakaoMap: MapView

    private var isMapInitialized: Boolean = false

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {

        }

        setUpKakaoMap()
    }

    private fun setUpKakaoMap() {
        kakaoMap = MapView(this)
        val mapViewContainer = binding.mapView
        mapViewContainer.addView(kakaoMap)
    }

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when (it) {
            is MyLocationState.Loading -> handelLoadingState()
            is MyLocationState.Success -> handSuccessState(it)
            is MyLocationState.Confirm -> handelConfirmState(it)
            is MyLocationState.Error -> Toast.makeText(this, it.messageId, Toast.LENGTH_SHORT)
                .show()
            else -> Unit
        }
    }

    private fun handelLoadingState() = with(binding) {
        locationLoading.toVisible()
        locationTitleTextView.text = getString(R.string.loading)
    }

    private fun handSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.toGone()
        locationTitleTextView.text = mapSearchInfo.fullAddress
        if (isMapInitialized.not()) {
            kakaoMap.setMapCenterPointAndZoomLevel(
                MapPoint.mapPointWithGeoCoord(
                    mapSearchInfo.locationLatLngEntity.latitude,
                    mapSearchInfo.locationLatLngEntity.longitude
                ), CAMERA_ZOOM_LEVEL, true
            )
            isMapInitialized = true
        }
        kakaoMap.setMapViewEventListener(this@MyLocationActivity)
    }

    private fun handelConfirmState(state: MyLocationState.Confirm) = with(binding) {

    }

    override fun onMapViewInitialized(p0: MapView?) {}
    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}

    override fun onMapViewMoveFinished(mapView: MapView, mapPoint: MapPoint) {
        Handler(Looper.getMainLooper()).postDelayed({
            val cameraLatLng = mapPoint.mapPointGeoCoord
            viewModel.changeLocationInfo(
                LocationLatLngEntity(
                    latitude = cameraLatLng.latitude,
                    longitude = cameraLatLng.longitude
                )
            )
        }, 1000)
    }
}