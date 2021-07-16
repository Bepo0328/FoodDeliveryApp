package kr.co.bepo.fooddeliveryapp.data.entity

data class MapSearchInfoEntity(
    override val id: Long = -1,
    val fullAddress: String,
    val name: String,
    val locationLatLngEntity: LocationLatLngEntity
): Entity