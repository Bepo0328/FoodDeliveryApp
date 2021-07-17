package kr.co.bepo.fooddeliveryapp.data.repository.user

import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLng: LocationLatLngEntity)

}