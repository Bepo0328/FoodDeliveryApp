package kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.restaurant

import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.databinding.ViewholderRestaurantBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.extensions.clear
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImageView.clear()
    }

    override fun bindData(model: RestaurantModel) = with(binding) {
        super.bindData(model)

        restaurantImageView.load(model.restaurantImageUrl, 24f)
        restaurantTitleTextView.text = model.restaurantTitle
        gradeTextView.text = resourcesProvider.getString(R.string.grad_format, model.grade)
        reviewCountTextView.text =
            resourcesProvider.getString(R.string.review_count, model.reviewCount)
        val (minTime, maxTime) = model.deliveryTimeRange
        deliveryTimeTextView.text =
            resourcesProvider.getString(R.string.delivery_time, minTime, maxTime)
        minPriceTexView.text = resourcesProvider.getString(R.string.min_price, model.minPrice)
        val (minTip, maxTip) = model.deliveryTipRange
        deliveryTipTextView.text =
            resourcesProvider.getString(R.string.delivery_tip, minTip, maxTip)

    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is RestaurantListListener) {
                root.setOnClickListener {
                    adapterListener.onClickItem(model)
                }
            }
        }

}