package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.R

enum class RestaurantDetailCategory(
    @StringRes val categoryNameId: Int
) {
    MENU(R.string.menu),
    REVIEW(R.string.review)
}