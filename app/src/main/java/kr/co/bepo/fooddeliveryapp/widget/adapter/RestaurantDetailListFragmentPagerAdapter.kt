package kr.co.bepo.fooddeliveryapp.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListFragment

class RestaurantDetailListFragmentPagerAdapter(
    activity: FragmentActivity,
    val fragmentList: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}