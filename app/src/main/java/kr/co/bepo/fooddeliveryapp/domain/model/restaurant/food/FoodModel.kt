package kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food

import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val restaurantId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String
): Model(id, type)