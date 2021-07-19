package kr.co.bepo.fooddeliveryapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantFoodEntity(
    val id: String,
    val restaurantId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String
) : Parcelable

