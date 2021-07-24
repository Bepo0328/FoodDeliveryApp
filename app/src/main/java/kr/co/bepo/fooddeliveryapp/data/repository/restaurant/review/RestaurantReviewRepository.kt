package kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): DefaultRestaurantReviewRepository.Result
}