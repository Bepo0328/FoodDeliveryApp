package kr.co.bepo.fooddeliveryapp.data.response.restaurant

import com.google.gson.annotations.SerializedName
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity

data class RestaurantFoodResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toEntity(restaurantId: Long, restaurantTitle: String) =
        RestaurantFoodEntity(
            id = id,
            restaurantId = restaurantId,
            title = title,
            description = description,
            price = price.toDouble().toInt(),
            imageUrl = imageUrl,
            restaurantTitle = restaurantTitle
        )
}