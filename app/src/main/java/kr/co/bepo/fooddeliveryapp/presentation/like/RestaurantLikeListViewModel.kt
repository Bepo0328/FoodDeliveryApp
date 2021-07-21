package kr.co.bepo.fooddeliveryapp.presentation.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.repository.user.UserRepository
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantLikeListViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantListLiveData.value = userRepository.getAllUserLikedRestaurantList().map {
            RestaurantModel(
                id = it.id,
                type = CellType.LIKE_RESTAURANT_CELL,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                minPrice = it.minPrice,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    fun disLikeRestaurant(restaurantEntity: RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(restaurantEntity.restaurantTitle)
        fetchData()
    }
}