package kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.android.material.appbar.AppBarLayout
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.databinding.ActivityRestaurantDetailBinding
import kr.co.bepo.fooddeliveryapp.extensions.fromDpToPx
import kr.co.bepo.fooddeliveryapp.extensions.load
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseActivity
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListFragment
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

        }
        likeButton.setOnClickListener {

        }
        shareButton.setOnClickListener {

        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            is RestaurantDetailState.Success -> handleSuccess(it)
        }
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
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

    }

}