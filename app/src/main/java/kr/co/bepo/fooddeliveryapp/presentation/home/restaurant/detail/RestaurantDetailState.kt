package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

sealed class RestaurantDetailState {

    object UnInitialized : RestaurantDetailState()

    object Loading : RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList: List<RestaurantFoodEntity>? = null,
        val foodMenuListInBasket: List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction: Pair<Boolean, () -> Unit> = Pair(false, {}),
        val isLiked: Boolean? = null
    ) : RestaurantDetailState()
}