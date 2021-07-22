package kr.co.bepo.fooddeliveryapp.data.repository.order

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ): DefaultOrderRepository.Result
}