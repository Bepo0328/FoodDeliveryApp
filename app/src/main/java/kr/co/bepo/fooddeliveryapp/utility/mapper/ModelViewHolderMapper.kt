package kr.co.bepo.fooddeliveryapp.utility.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import kr.co.bepo.fooddeliveryapp.databinding.*
import kr.co.bepo.fooddeliveryapp.domain.model.CellType
import kr.co.bepo.fooddeliveryapp.domain.model.Model
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.EmptyViewHolder
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.restaurant.RestaurantLikeViewHolder
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.restaurant.food.FoodMenuViewHolder
import kr.co.bepo.fooddeliveryapp.widget.adapter.viewholder.restaurant.review.ReviewViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKE_RESTAURANT_CELL -> RestaurantLikeViewHolder(
                ViewholderRestaurantLikeBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> ReviewViewHolder(
                ViewholderReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }

        return viewHolder as ModelViewHolder<M>
    }
}