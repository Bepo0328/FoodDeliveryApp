package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant

import android.util.Log
import androidx.core.os.bundleOf
import kr.co.bepo.fooddeliveryapp.databinding.FragmentRestaurantListBinding
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment() :
    BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {

    companion object {
        private const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"

        fun newInstance(restaurantCategory: RestaurantCategory): RestaurantListFragment =
            RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory
                )
            }
    }

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }

    override val viewModel: RestaurantListViewModel by viewModel { parametersOf(restaurantCategory) }

    override fun getViewBinding(): FragmentRestaurantListBinding =
        FragmentRestaurantListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        Log.d("restaurantList", it.toString())
    }

}