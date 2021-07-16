package kr.co.bepo.fooddeliveryapp.data.response.address

import com.google.gson.annotations.SerializedName
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity

data class AddressInfo(
    @SerializedName("addressKey")
    val addressKey: String? = null,
    @SerializedName("addressType")
    val addressType: String? = null,
    @SerializedName("adminDong")
    val adminDong: String? = null,
    @SerializedName("adminDongCode")
    val adminDongCode: String? = null,
    @SerializedName("buildingIndex")
    val buildingIndex: String? = null,
    @SerializedName("buildingName")
    val buildingName: String? = null,
    @SerializedName("bunji")
    val bunji: String? = null,
    @SerializedName("city_do")
    val cityDo: String? = null,
    @SerializedName("eup_myun")
    val eupMyun: String? = null,
    @SerializedName("fullAddress")
    val fullAddress: String? = null,
    @SerializedName("gu_gun")
    val guGun: String? = null,
    @SerializedName("legalDong")
    val legalDong: String? = null,
    @SerializedName("legalDongCode")
    val legalDongCode: String? = null,
    @SerializedName("mappingDistance")
    val mappingDistance: String? = null,
    @SerializedName("ri")
    val ri: String? = null,
    @SerializedName("roadAddressKey")
    val roadAddressKey: String? = null,
    @SerializedName("roadCode")
    val roadCode: String? = null,
    @SerializedName("roadName")
    val roadName: String? = null
) {
    fun toSearchInfoEntity(locationLatLngEntity: LocationLatLngEntity) = MapSearchInfoEntity(
        fullAddress = fullAddress ?: "주소 정보 없음",
        name = buildingName ?: "빌딩 정보 없음",
        locationLatLngEntity = locationLatLngEntity
    )
}