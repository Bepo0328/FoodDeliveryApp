package kr.co.bepo.fooddeliveryapp.domain.model.restaurant

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.Model
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory

data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val minPrice: Int,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?
) : Model(id, type) {

    fun toEntity() = RestaurantEntity(
        id = id,
        restaurantInfoId = restaurantInfoId,
        restaurantCategory = restaurantCategory,
        restaurantTitle = restaurantTitle,
        restaurantImageUrl = restaurantImageUrl,
        grade = grade,
        reviewCount = reviewCount,
        deliveryTimeRange = deliveryTimeRange,
        minPrice = minPrice,
        deliveryTipRange = deliveryTipRange,
        restaurantTelNumber = restaurantTelNumber
    )
}