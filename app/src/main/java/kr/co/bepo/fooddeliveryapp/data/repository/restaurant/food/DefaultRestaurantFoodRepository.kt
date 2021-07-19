package kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.bepo.fooddeliveryapp.data.api.FoodApiService
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

class DefaultRestaurantFoodRepository(
    private val foodApiService: FoodApiService,
    private val ioDispatcher: CoroutineDispatcher
): RestaurantFoodRepository {

    override suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher) {
        val response = foodApiService.getRestaurantFoods(restaurantId)
        if (response.isSuccessful) {
            response.body()?.map { it.toEntity(restaurantId) } ?: listOf()
        } else {
            listOf()
        }
    }

}