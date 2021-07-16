package kr.co.bepo.fooddeliveryapp.data.response.search


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("searchPoiInfo")
    val searchPoiInfo: SearchPoiInfo? = null
)