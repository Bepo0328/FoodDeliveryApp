package kr.co.bepo.fooddeliveryapp.presentation.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import kr.co.bepo.fooddeliveryapp.databinding.FragmentRestaurantLikeListBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.RestaurantModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.RestaurantDetailActivity
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment :
    BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override val viewModel: RestaurantLikeListViewModel by viewModel()

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantLikeListListener {
                override fun onDisLikeItem(model: RestaurantModel) {
                    viewModel.disLikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun getViewBinding(): FragmentRestaurantLikeListBinding =
        FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) = with(binding) {
        val isEmpty = restaurantList.isEmpty()
        recyclerView.isGone = isEmpty
        emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }
}