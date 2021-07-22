package kr.co.bepo.fooddeliveryapp.presentation.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import kr.co.bepo.fooddeliveryapp.databinding.ActivityOrderMenuListBinding
import kr.co.bepo.fooddeliveryapp.domain.model.restaurant.food.FoodModel
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseActivity
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import kr.co.bepo.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import kr.co.bepo.fooddeliveryapp.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    companion object {
        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }

    override val viewModel: OrderMenuListViewModel by viewModel()

    private val resourcesProvider: ResourcesProvider by inject()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : OrderMenuListListener {
                override fun onRemoveItem(model: FoodModel) {
                    viewModel.removeOrderMenu(model)
                }
            }
        )
    }


    override fun getViewBinding(): ActivityOrderMenuListBinding =
        ActivityOrderMenuListBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        recyclerVIew.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this) {
        when (it) {
            is OrderMenuListState.Loading -> handleLoadingState()
            is OrderMenuListState.Success -> handleSuccessState(it)
            is OrderMenuListState.Order -> handleOrderState()
            is OrderMenuListState.Error -> handleErrorState(it)
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.toVisible()
    }

    private fun handleSuccessState(state: OrderMenuListState.Success) = with(binding) {
        progressBar.toGone()
        adapter.submitList(state.restaurantFoodModelList)
        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()

        if (menuOrderIsEmpty) {
            Toast.makeText(this@OrderMenuListActivity, "주문 메뉴가 없어 화면을 종료합니다.", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun handleOrderState() = with(binding) {
        Toast.makeText(this@OrderMenuListActivity, "성공적으로 주문을 완료하였습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleErrorState(state: OrderMenuListState.Error) = with(binding) {
        Toast.makeText(this@OrderMenuListActivity, getString(state.messageId, state.e), Toast.LENGTH_SHORT).show()
    }


}