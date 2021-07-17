package kr.co.bepo.fooddeliveryapp.presentation.myloaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.data.repository.map.MapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.UserRepository
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myLocationStateLiveData.value = MyLocationState.Loading
        myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    fun changeLocationInfo(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            myLocationStateLiveData.value = MyLocationState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun confirmSelectLocation() = viewModelScope.launch {
        when (val data = myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLngEntity)
                myLocationStateLiveData.value = MyLocationState.Confirm(
                    data.mapSearchInfoEntity
                )
            }
        }
    }

}