package kr.co.bepo.fooddeliveryapp.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@androidx.room.Entity
data class LocationLatLngEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = -1,
    val latitude: Double,
    val longitude: Double
) : Entity, Parcelable