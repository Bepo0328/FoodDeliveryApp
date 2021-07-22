package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.databinding.ActivityRestaurantDetailBinding
import kr.co.bepo.fooddeliveryapp.extensions.fromDpToPx
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseActivity
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.menu.RestaurantMenuListFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review.RestaurantReviewListFragment
import kr.co.bepo.fooddeliveryapp.presentation.main.MainActivity
import kr.co.bepo.fooddeliveryapp.presentation.main.MainTabMenu
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListActivity
import kr.co.bepo.fooddeliveryapp.utility.event.MenuChangeEventBus
import kr.co.bepo.fooddeliveryapp.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs


class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }

    override val viewModel: RestaurantDetailViewModel by viewModel {
        parametersOf(
            intent.getParcelableExtra(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val menuChangeEventBus: MenuChangeEventBus by inject()

    override fun getViewBinding(): ActivityRestaurantDetailBinding =
        ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override fun initViews() {
        initAppBar()
    }

    private fun initAppBar() = with(binding) {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val location: IntArray = intArrayOf(0, 0)
            ratingBar.getLocationInWindow(location)

            val topPadding = location[0].fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset: Float =
                if (abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        toolbar.setNavigationOnClickListener { finish() }

        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                if (telNumber.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@RestaurantDetailActivity,
                        "전화 번호가 없습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        likeButton.setOnClickListener {
            viewModel.toggleLikeRestaurant()
        }
        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "음식점 이름 : ${restaurantInfo.restaurantTitle}\n" +
                                "평점 : ${restaurantInfo.grade}\n" +
                                "연락처 : ${restaurantInfo.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "친구에게 공유하기")
                }
                startActivity(intent)
            }
        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            is RestaurantDetailState.Success -> handleSuccessState(it)
            is RestaurantDetailState.Loading -> handleLoadingState()
            else -> Unit
        }
    }

    private fun handleSuccessState(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.toGone()

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImageView.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        ratingTextView.text = restaurantEntity.grade.toString()
        deliveryTimeTextView.text =
            getString(
                R.string.delivery_expected_time,
                restaurantEntity.deliveryTimeRange.first,
                restaurantEntity.deliveryTimeRange.second
            )
        deliveryTipTextView.text =
            getString(
                R.string.delivery_tip_range,
                restaurantEntity.deliveryTipRange.first,
                restaurantEntity.deliveryTipRange.second
            )
        likeTextView.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ), null, null, null
        )

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(
                state.restaurantEntity.restaurantInfoId,
                state.restaurantEntity.restaurantTitle,
                state.restaurantFoodList
            )
        }

        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
        if (isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }

    }

    private fun handleLoadingState() = with(binding) {
        progressBar.toVisible()
    }

    private fun initViewPager(
        restaurantInfoId: Long,
        restaurantTitle: String,
        restaurantFoodList: List<RestaurantFoodEntity>?
    ) = with(binding) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this@RestaurantDetailActivity,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                )
            )
        )

        menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(menuAndReviewTabLayout, menuAndReviewViewPager) { tab, position ->
            tab.setText(RestaurantDetailCategory.values()[position].categoryNameId)
        }.attach()
    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) =
        with(binding) {
            basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()) {
                basketGroup.toGone()
                "0"
            } else {
                basketGroup.toVisible()
                getString(R.string.basket_count, foodMenuListInBasket.size)
            }

            basketButton.setOnClickListener {
                if (firebaseAuth.currentUser == null) {
                    alertLoginNeed {
                        lifecycleScope.launch {
                            menuChangeEventBus.changeMenu(MainTabMenu.MY)
                            finish()
                        }
                    }
                } else {
                    startActivity(OrderMenuListActivity.newIntent(this@RestaurantDetailActivity))
                }
            }
        }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에 같은 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우 이전에 담은 메뉴가 삭제됩니다.")
            .setPositiveButton("담기") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("로그인이 필요합니다.")
            .setMessage("주문하려면 로그인이 필요합니다.\nMy 탭으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}