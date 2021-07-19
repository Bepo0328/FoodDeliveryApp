package kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant

import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface RestaurantListListener : AdapterListener {
    fun onClickItem(model: RestaurantModel)
}