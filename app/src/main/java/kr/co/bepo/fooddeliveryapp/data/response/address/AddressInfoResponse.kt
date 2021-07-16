package kr.co.bepo.fooddeliveryapp.data.response.address

import com.google.gson.annotations.SerializedName

data class AddressInfoResponse(
    @SerializedName("addressInfo")
    val addressInfo: AddressInfo? = null
)