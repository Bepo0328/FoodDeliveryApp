package kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant

import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {

    fun onClickItem(model: FoodModel)
}