package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review

import androidx.core.os.bundleOf
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.databinding.FragmentListBinding
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import org.koin.android.ext.android.inject

class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"

        fun newInstance(restaurantId: Long): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }

    override val viewModel: RestaurantReviewListViewModel by inject()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun observeData() {

    }
}