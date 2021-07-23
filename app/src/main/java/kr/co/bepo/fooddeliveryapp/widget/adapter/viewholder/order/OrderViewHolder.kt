package kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.order

import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.databinding.ViewholderOrderBinding
import kr.co.bepo.fooddeliveryapp.domain.model.order.OrderModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.order.OrderListListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: OrderModel) = with(binding) {
        super.bindData(model)
        orderTitleTextView.text =
            resourcesProvider.getString(R.string.order_history_title, model.orderId)

        val foodMenuList = model.foodMenuList

        foodMenuList
            .groupBy { it.title }
            .entries.forEach { (title, menuList) ->
                val orderDataStr =
                    orderContentTextView.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
                orderContentTextView.text = orderDataStr
            }
        orderContentTextView.text = orderContentTextView.text.trim()
        orderTotalPriceText.text = resourcesProvider.getString(
            R.string.price,
            foodMenuList.map { it.price }.reduce { total, price -> total + price })
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {

        if (adapterListener is OrderListListener) {
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId, model.restaurantTitle)
            }
        }
    }
}