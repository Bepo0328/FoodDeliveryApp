package kr.co.bepo.fooddeliveryapp.presentation.home

import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity

sealed class HomeState {

    object UnInitialized : HomeState()

    object Loading : HomeState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ) : HomeState()

    data class Error(
        @StringRes val messageId: Int
    ) : HomeState()
}