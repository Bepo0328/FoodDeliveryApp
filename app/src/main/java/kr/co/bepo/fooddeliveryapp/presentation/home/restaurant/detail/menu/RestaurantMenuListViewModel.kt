package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    val restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()
    val menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()
    val isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean, () -> Unit>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantFoodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                restaurantId = restaurantId,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                foodId = it.id
            )
        }
    }

    fun insertMenuInBasket(foodModel: FoodModel) = viewModelScope.launch {
        val restaurantMenuListInBasket =
            restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        val foodMenuEntity = foodModel.toEntity(restaurantMenuListInBasket.size)
        val anotherRestaurantMenuListInBasket =
            restaurantFoodRepository.getAllFoodMenuListInBasket()
                .filter { it.restaurantId != restaurantId }
        if (anotherRestaurantMenuListInBasket.isNotEmpty()) {
            isClearNeedInBasketLiveData.value =
                Pair(true, { clearMenuAndInsertNewMenuInBasket(foodMenuEntity) })
        } else {
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            menuBasketLiveData.value = foodMenuEntity
        }
    }

    private fun clearMenuAndInsertNewMenuInBasket(foodMenuEntity: RestaurantFoodEntity) =
        viewModelScope.launch {
            restaurantFoodRepository.clearFoodMenuInBasket()
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            menuBasketLiveData.value = foodMenuEntity
        }
}