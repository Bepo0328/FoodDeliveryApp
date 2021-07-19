package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity
) : BaseViewModel() {

    val restaurantDetailStateLiveData =
        MutableLiveData<RestaurantDetailState>(RestaurantDetailState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
    }

    fun getRestaurantTelNumber(): String? =
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
}