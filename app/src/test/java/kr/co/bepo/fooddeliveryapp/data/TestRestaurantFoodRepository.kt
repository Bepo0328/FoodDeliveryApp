package kr.co.bepo.fooddeliveryapp.data

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository

class TestRestaurantFoodRepository: RestaurantFoodRepository {

    private val foodMenuListInBasket = mutableListOf<RestaurantFoodEntity>()

    override suspend fun getFoods(
        restaurantId: Long,
        restaurantTitle: String
    ): List<RestaurantFoodEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity> {
        return foodMenuListInBasket
    }

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) {
        foodMenuListInBasket.add(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuInBasket(foodId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearFoodMenuInBasket() {
        foodMenuListInBasket.clear()
    }
}