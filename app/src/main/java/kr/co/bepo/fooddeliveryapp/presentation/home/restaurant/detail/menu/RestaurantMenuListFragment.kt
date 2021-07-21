package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.databinding.FragmentListBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.RestaurantDetailViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment :
    BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"
        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(
            restaurantId: Long,
            foodList: ArrayList<RestaurantFoodEntity>
        ): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }
    private val restaurantFoodList by lazy {
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(
            FOOD_LIST_KEY
        )
    }

    override val viewModel: RestaurantMenuListViewModel by viewModel() {
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    private val restaurantDetailViewModel: RestaurantDetailViewModel by sharedViewModel()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : FoodMenuListListener {
                override fun onClickItem(model: FoodModel) {
                    viewModel.insertMenuInBasket(model)
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() {

        viewModel.restaurantFoodListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.menuBasketLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "장바구니에 담겼습니다.\n메뉴: ${it.title}", Toast.LENGTH_SHORT)
                .show()
            restaurantDetailViewModel.notifyFoodMenuListInBasket(it)
        }

        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if (isClearNeed) {
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }

        }

    }
}