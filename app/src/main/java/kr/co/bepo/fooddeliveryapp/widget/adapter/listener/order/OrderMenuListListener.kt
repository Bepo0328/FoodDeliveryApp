package kr.co.bepo.fooddeliveryapp.widget.adapter.listener.order

import kr.co.bepo.fooddeliveryapp.domain.model.food.FoodModel
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {

    fun onRemoveItem(model: FoodModel)

}