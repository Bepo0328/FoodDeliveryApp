package kr.co.bepo.fooddeliveryapp.presentation.myloaction

import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity

sealed class MyLocationState {

    object UnInitialized : MyLocationState()

    object Loading : MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ) : MyLocationState()
}