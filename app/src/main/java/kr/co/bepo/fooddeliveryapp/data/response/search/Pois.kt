package kr.co.bepo.fooddeliveryapp.data.response.search


import com.google.gson.annotations.SerializedName

data class Pois(
    @SerializedName("poi")
    val poi: List<Poi>? = null
)