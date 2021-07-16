package kr.co.bepo.fooddeliveryapp.data.response.search


import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("buildingNo1")
    val buildingNo1: String? = null,
    @SerializedName("buildingNo2")
    val buildingNo2: String? = null,
    @SerializedName("dataKind")
    val dataKind: String? = null,
    @SerializedName("detailAddrName")
    val detailAddrName: String? = null,
    @SerializedName("firstNo")
    val firstNo: String? = null,
    @SerializedName("frontLat")
    val frontLat: String? = null,
    @SerializedName("frontLon")
    val frontLon: String? = null,
    @SerializedName("ggPrice")
    val ggPrice: String? = null,
    @SerializedName("hhPrice")
    val hhPrice: String? = null,
    @SerializedName("highGpPrice")
    val highGpPrice: String? = null,
    @SerializedName("highHhPrice")
    val highHhPrice: String? = null,
    @SerializedName("highHhSale")
    val highHhSale: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("llPrice")
    val llPrice: String? = null,
    @SerializedName("lowerAddrName")
    val lowerAddrName: String? = null,
    @SerializedName("merchantFlag")
    val merchantFlag: String? = null,
    @SerializedName("middleAddrName")
    val middleAddrName: String? = null,
    @SerializedName("minOilYn")
    val minOilYn: String? = null,
    @SerializedName("mlClass")
    val mlClass: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("noorLat")
    val noorLat: String? = null,
    @SerializedName("noorLon")
    val noorLon: String? = null,
    @SerializedName("oilBaseSdt")
    val oilBaseSdt: String? = null,
    @SerializedName("parkFlag")
    val parkFlag: String? = null,
    @SerializedName("pkey")
    val pkey: String? = null,
    @SerializedName("radius")
    val radius: String? = null,
    @SerializedName("roadName")
    val roadName: String? = null,
    @SerializedName("rpFlag")
    val rpFlag: String? = null,
    @SerializedName("secondNo")
    val secondNo: String? = null,
    @SerializedName("stId")
    val stId: String? = null,
    @SerializedName("telNo")
    val telNo: String? = null,
    @SerializedName("upperAddrName")
    val upperAddrName: String? = null
)