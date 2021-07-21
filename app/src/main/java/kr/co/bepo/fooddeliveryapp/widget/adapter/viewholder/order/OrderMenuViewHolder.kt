package kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.order

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.databinding.ViewholderFoodMenuBinding
import kr.co.bepo.fooddeliveryapp.databinding.ViewholderOrderMenuBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.extensions.clear
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.order.OrderMenuListListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderMenuViewHolder(
    private val binding: ViewholderOrderMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        foodImageView.clear()
    }

    override fun bindData(model: FoodModel) = with(binding) {
        super.bindData(model)
        foodImageView.load(model.imageUrl, 24f, CenterCrop())
        foodTitleTextView.text = model.title
        foodDescriptionTextView.text = model.description
        priceTextView.text = resourcesProvider.getString(R.string.price, model.price)
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is OrderMenuListListener) {
            removeImageView.setOnClickListener {
                adapterListener.onRemoveItem(model)
            }
        }
    }


}