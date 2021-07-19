package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant

import android.util.Log
import androidx.core.os.bundleOf
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.databinding.FragmentRestaurantListBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.RestaurantDetailActivity
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment :
    BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>() {

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        const val RESTAURANT_KEY = "Restaurant"

        fun newInstance(
            restaurantCategory: RestaurantCategory,
            locationLatLng: LocationLatLngEntity
        ): RestaurantListFragment =
            RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory,
                    LOCATION_KEY to locationLatLng
                )
            }
    }

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    private val locationLatLng: LocationLatLngEntity by lazy { arguments?.getParcelable(LOCATION_KEY)!! }
    override val viewModel: RestaurantListViewModel by viewModel {
        parametersOf(
            restaurantCategory,
            locationLatLng
        )
    }

    override fun getViewBinding(): FragmentRestaurantListBinding =
        FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantListListener {
                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(
                            requireContext(),
                            model.toEntity()
                        )
                    )
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        Log.d("restaurantList", it.toString())
        adapter.submitList(it)
    }

}