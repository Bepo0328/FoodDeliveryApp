package kr.co.bepo.fooddeliveryapp.widget.adapter.listener.order

import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface OrderListListener : AdapterListener {

    fun writeRestaurantReview(orderId: String, restaurantTitle: String)
}