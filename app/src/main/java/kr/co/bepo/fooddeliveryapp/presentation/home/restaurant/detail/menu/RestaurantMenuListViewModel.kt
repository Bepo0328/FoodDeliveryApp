package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>
) : BaseViewModel() {

    val restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantFoodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                restaurantId = restaurantId,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl
            )
        }
    }
}