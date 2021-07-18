package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity

sealed class RestaurantDetailState {

    object UnInitialized : RestaurantDetailState()

    object Loading : RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val isLiked: Boolean? = null
    ) : RestaurantDetailState()
}