package kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>
}