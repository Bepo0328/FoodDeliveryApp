package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant

import androidx.annotation.StringRes
import kr.co.bepo.fooddeliveryapp.R

enum class RestaurantCategory(
    @StringRes val categoryNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    ALL(R.string.all, R.string.all_type),
    KOREAN_FOOD(R.string.korean_food, R.string.korean_food_type),
    DUMPLING_FOOD(R.string.dumpling_food, R.string.dumpling_food_type),
    JAPANESE_FOOD(R.string.japanese_food, R.string.japanese_food_type),
    CHICKEN(R.string.chicken, R.string.chicken_type),
    PIZZA(R.string.pizza, R.string.pizza_type),
    ASIAN_EUROPE_FOOD(R.string.asian_europe_food, R.string.asian_europe_food_type),
    CHINESE_FOOD(R.string.chinese_food, R.string.chinese_food_type),
    JOKBAL_BOSSAM(R.string.jokbal_bossam, R.string.jokbal_bossam_type),
    MIDNIGHT_FOOD(R.string.midnight_food, R.string.midnight_food_type),
    JJIM_TANG(R.string.jjim_tang, R.string.jjim_tang_type),
    PACKED_LUNCH(R.string.packed_lunch, R.string.packed_lunch_type),
    CAFE_DESSERT(R.string.cafe_dessert, R.string.cafe_dessert_type),
    FAST_FOOD(R.string.fast_food, R.string.fast_food_type)
}