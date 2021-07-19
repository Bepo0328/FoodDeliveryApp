package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.UserRepository
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val restaurantDetailStateLiveData =
        MutableLiveData<RestaurantDetailState>(RestaurantDetailState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val isLiked =
            userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        val foods = restaurantFoodRepository.getFoods(restaurantEntity.restaurantInfoId)
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked = isLiked
        )
    }

    fun getRestaurantTelNumber(): String? =
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }

    fun getRestaurantInfo(): RestaurantEntity? =
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }

    fun toggleLikeRestaurant() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(isLiked = false)
                } ?: kotlin.run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    restaurantDetailStateLiveData.value = data.copy(isLiked = true)
                }
            }
        }
    }

    fun notifyFoodMenuListInBasket(restaurantFoodEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            when (val data = restaurantDetailStateLiveData.value) {
                is RestaurantDetailState.Success -> {
                    restaurantDetailStateLiveData.value = data.copy(
                        foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                            add(restaurantFoodEntity)
                        }
                    )
                }
                else -> Unit
            }
        }

    fun notifyClearNeedAlertInBasket(clearNeed: Boolean, afterAction: () -> Unit) {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(clearNeed, afterAction)
                )
            }
            else -> Unit
        }
    }

    fun notifyClearBasket() = viewModelScope.launch {
        when (val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                )
            }
            else -> Unit
        }
    }
}