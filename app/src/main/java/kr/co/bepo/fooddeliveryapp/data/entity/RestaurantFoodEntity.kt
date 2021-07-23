package kr.co.bepo.fooddeliveryapp.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RestaurantFoodEntity(
    @PrimaryKey val id: String,
    val restaurantId: Long,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantTitle: String
) : Parcelable

