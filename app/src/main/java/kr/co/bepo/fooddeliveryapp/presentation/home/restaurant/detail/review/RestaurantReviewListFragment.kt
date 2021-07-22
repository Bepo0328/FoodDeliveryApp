package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review

import androidx.core.os.bundleOf
import kr.co.bepo.fooddeliveryapp.databinding.FragmentListBinding
import kr.co.bepo.fooddeliveryapp.domain.model.review.ReviewModel
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    companion object {

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }

    override val viewModel: RestaurantReviewListViewModel by viewModel {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<ReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {}
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is RestaurantReviewState.Success -> handleSuccessState(it)
            else -> Unit
        }
    }

    private fun handleSuccessState(state: RestaurantReviewState.Success) = with(binding) {
        adapter.submitList(state.reviewList)
    }

}