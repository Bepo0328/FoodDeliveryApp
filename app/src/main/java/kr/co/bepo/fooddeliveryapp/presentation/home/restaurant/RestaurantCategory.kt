package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant

import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    ALL(R.string.all, R.string.all_type)
}