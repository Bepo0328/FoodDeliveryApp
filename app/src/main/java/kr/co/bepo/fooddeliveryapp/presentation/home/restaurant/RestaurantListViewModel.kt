package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLng: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT
) : BaseViewModel() {

    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLng)
        val sortedList = when (restaurantOrder) {
            RestaurantOrder.DEFAULT -> restaurantList
            RestaurantOrder.FAST_DELIVERY -> restaurantList.sortedBy { it.deliveryTimeRange.first }
            RestaurantOrder.LOW_DELIVERY_TIP -> restaurantList.sortedBy { it.deliveryTipRange.first }
            // TODO 현재는 리뷰 카운트 지만 주문량 체크 후 교체 필요.
            RestaurantOrder.MANY_ORDER -> restaurantList.sortedByDescending { it.reviewCount }
            RestaurantOrder.TOP_RATE -> restaurantList.sortedByDescending { it.grade }
        }
        restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                minPrice = it.minPrice,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    fun setLocationLatLng(locationLatLng: LocationLatLngEntity) {
        this.locationLatLng = locationLatLng
        fetchData()
    }

    fun setRestaurantOrder(restaurantOrder: RestaurantOrder) {
        this.restaurantOrder = restaurantOrder
        fetchData()
    }
}