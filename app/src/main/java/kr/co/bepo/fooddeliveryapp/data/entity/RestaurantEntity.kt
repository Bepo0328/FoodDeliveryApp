package kr.co.bepo.fooddeliveryapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory

@Parcelize
data class RestaurantEntity(
    override val id: Long,
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
) : Entity, Parcelable