package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review

import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.review.ReviewModel

sealed class RestaurantReviewState {

    object UnInitialized : RestaurantReviewState()

    object Loading : RestaurantReviewState()

    data class Success(
        val reviewList: List<ReviewModel>
    ) : RestaurantReviewState()
}