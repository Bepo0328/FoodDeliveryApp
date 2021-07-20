package kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): List<RestaurantReviewEntity>
}