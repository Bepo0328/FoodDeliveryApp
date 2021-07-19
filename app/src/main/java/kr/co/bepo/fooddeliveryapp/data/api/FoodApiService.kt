package kr.co.bepo.fooddeliveryapp.data.api

import kr.co.bepo.fooddeliveryapp.data.response.restaurant.RestaurantFoodResponse
import kr.co.bepo.fooddeliveryapp.data.url.Url
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {

    @GET(Url.GET_RESTAURANT_FOOD)
    suspend fun getRestaurantFoods(
        @Path("restaurantId") restaurantId: Long
    ): Response<List<RestaurantFoodResponse>>
}