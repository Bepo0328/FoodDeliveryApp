package kr.co.bepo.fooddeliveryapp.data.response.search


import com.google.gson.annotations.SerializedName

data class SearchPoiInfo(
    @SerializedName("count")
    val count: String? = null,
    @SerializedName("page")
    val page: String? = null,
    @SerializedName("pois")
    val pois: Pois? = null,
    @SerializedName("totalCount")
    val totalCount: String? = null
)