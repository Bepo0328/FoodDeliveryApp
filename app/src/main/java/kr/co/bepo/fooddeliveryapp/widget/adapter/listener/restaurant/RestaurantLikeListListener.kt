package kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant

import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel

interface RestaurantLikeListListener : RestaurantListListener {

    fun onDisLikeItem(model: RestaurantModel)
}