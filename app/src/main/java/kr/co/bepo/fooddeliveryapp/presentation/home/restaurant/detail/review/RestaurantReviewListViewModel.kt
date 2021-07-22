package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import kr.co.bepo.fooddeliveryapp.domain.model.review.ReviewModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading

        val reviews = restaurantReviewRepository.getReviews(restaurantTitle)
        reviewStateLiveData.value = RestaurantReviewState.Success(
            reviews.map {
                ReviewModel(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    grade = it.grade,
                    thumbnailImageUri = it.images?.first()
                )
            }
        )
    }
}