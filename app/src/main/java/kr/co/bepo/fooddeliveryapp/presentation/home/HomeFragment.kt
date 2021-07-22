package kr.co.bepo.fooddeliveryapp.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kr.co.bepo.fooddeliveryapp.R
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.databinding.FragmentHomeBinding
import kr.co.bepo.fooddeliveryapp.extensions.toGone
import kr.co.bepo.fooddeliveryapp.extensions.toVisible
import kr.co.bepo.fooddeliveryapp.presentation.base.BaseFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListFragment
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantOrder
import kr.co.bepo.fooddeliveryapp.presentation.main.MainActivity
import kr.co.bepo.fooddeliveryapp.presentation.main.MainTabMenu
import kr.co.bepo.fooddeliveryapp.presentation.myloaction.MyLocationActivity
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListActivity
import kr.co.bepo.fooddeliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override val viewModel: HomeViewModel by viewModel()

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private var isShowBasket: Boolean = false
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInfo ->
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLngEntity)
                    }
            }
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermission = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (responsePermission.filter { it.value == true }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleTextView) {
                    setText(R.string.please_setup_your_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.can_not_assigned_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun initViews() = with(binding) {
        locationTitleTextView.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(),
                        mapInfo
                    )
                )
            }
        }

        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.toGone()
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)

                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true

                }
                R.id.chipFastDelivery -> {
                    chipInitialize.toVisible()
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

                }
                R.id.chipLowDeliveryTip -> {
                    chipInitialize.toVisible()
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)

                }
                R.id.chipManyOrder -> {
                    chipInitialize.toVisible()
                    changeRestaurantOrder(RestaurantOrder.MANY_ORDER)

                }
                R.id.chipTopRate -> {
                    chipInitialize.toVisible()
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)

                }
            }
        }
    }

    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }


    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.UnInitialized ->
                    getMyLocation()
                is HomeState.Loading -> {
                    binding.basketGroup.toGone()
                    binding.locationLoading.toVisible()
                    binding.locationTitleTextView.text = getString(R.string.loading)
                }
                is HomeState.Success -> {
                    if (isShowBasket) {
                        binding.basketGroup.toVisible()
                    }
                    binding.locationLoading.toGone()
                    binding.loadingSuccessGroup.toVisible()
                    binding.locationTitleTextView.text = it.mapSearchInfoEntity.fullAddress
                    initViewPager(it.mapSearchInfoEntity.locationLatLngEntity)
                    if (it.isLocationSame.not()) {
                        Toast.makeText(
                            requireContext(),
                            R.string.please_set_your_current_location,
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.loadingSuccessGroup.toGone()
                    }
                }
                is HomeState.Error -> {
                    binding.basketGroup.toGone()
                    binding.locationLoading.toGone()
                    binding.loadingSuccessGroup.toGone()
                    binding.locationTitleTextView.setText(R.string.location_not_found)
                    Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.foodMenuBasketLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                isShowBasket = true
                if (viewModel.homeStateLiveData.value is HomeState.Success) {
                    binding.basketGroup.toVisible()
                }
                binding.basketCountTextView.text = getString(R.string.basket_count, it.size)
                binding.basketButton.setOnClickListener {
                    if (firebaseAuth.currentUser == null) {
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                        }
                    } else {
                        startActivity(OrderMenuListActivity.newIntent(requireContext()))
                    }
                }
            } else {
                isShowBasket = false
                binding.basketGroup.toGone()
            }
        }
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
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

    override fun onResume() {
        super.onResume()
        viewModel.checkMyBasket()
    }

    private fun initViewPager(locationLatLng: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLng)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLng
            )
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }

        if (locationLatLng != viewPagerAdapter.locationLatLng) {
            viewPagerAdapter.locationLatLng = locationLatLng

            viewPagerAdapter.fragmentList.forEach {
                try {
                    it.viewModel.setLocationLatLng(locationLatLng)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener : LocationListener {

        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            removeLocationListener()
        }
    }

}