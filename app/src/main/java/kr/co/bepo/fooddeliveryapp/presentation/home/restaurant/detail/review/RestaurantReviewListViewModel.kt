package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.ReviewEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import kr.co.bepo.fooddeliveryapp.domain.model.review.ReviewModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {

    val reviewStateLiveData =
        MutableLiveData<RestaurantReviewState>(RestaurantReviewState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value = RestaurantReviewState.Loading

        when (val result = restaurantReviewRepository.getReviews(restaurantTitle)) {
            is DefaultRestaurantReviewRepository.Result.Success -> {
                val reviews = result.data as List<ReviewEntity>
                reviewStateLiveData.value = RestaurantReviewState.Success(
                    reviews.map {
                        ReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if (it.imageUrlList.isNullOrEmpty()) {
                                null
                            } else {
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> Unit
        }
    }
}