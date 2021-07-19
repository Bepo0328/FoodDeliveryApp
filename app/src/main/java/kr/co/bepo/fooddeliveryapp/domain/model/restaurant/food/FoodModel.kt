package kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food

import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val restaurantId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val foodId: String
): Model(id, type) {

    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        id = "${foodId}_${basketIndex}",
        restaurantId = restaurantId,
        title = title,
        description = description,
        price = price,
        imageUrl = imageUrl
    )
}