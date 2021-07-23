package kr.co.bepo.fooddeliveryapp.data.entity

import android.util.FloatMath

data class ReviewEntity(
    val userId: String,
    val title: String,
    val createdAt: Long,
    val content: String,
    val rating: Float,
    val imageUrlList: List<String>? = null,
    val orderId: String,
    val restaurantTitle: String
)