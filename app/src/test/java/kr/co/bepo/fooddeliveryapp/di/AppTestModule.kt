package kr.co.bepo.fooddeliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import kr.co.bepo.fooddeliveryapp.data.TestOrderRepository
import kr.co.bepo.fooddeliveryapp.data.TestRestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.data.TestRestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.repository.order.OrderRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLngEntity, get())
    }

    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }

    single<RestaurantRepository> { TestRestaurantRepository() }
    single<RestaurantFoodRepository> { TestRestaurantFoodRepository() }
    single<OrderRepository> { TestOrderRepository() }
}