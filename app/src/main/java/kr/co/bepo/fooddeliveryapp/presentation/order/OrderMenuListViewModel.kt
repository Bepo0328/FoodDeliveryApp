package kr.co.bepo.fooddeliveryapp.presentation.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val orderMenuStateLiveData = MutableLiveData<OrderMenuListState>(OrderMenuListState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        orderMenuStateLiveData.value = OrderMenuListState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        orderMenuStateLiveData.value = OrderMenuListState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id
                )
            }
        )
    }

    fun orderMenu() {

    }

    fun clearOrderMenu() {

    }

    fun removeOrderMenu(model: FoodModel) {

    }
}