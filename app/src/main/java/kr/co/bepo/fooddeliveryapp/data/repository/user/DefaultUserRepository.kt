package kr.co.bepo.fooddeliveryapp.data.repository.user

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.fooddeliveryapp.data.db.dao.LocationDao
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLng: LocationLatLngEntity) = withContext(ioDispatcher) {
        locationDao.insert(locationLatLng)
    }
}