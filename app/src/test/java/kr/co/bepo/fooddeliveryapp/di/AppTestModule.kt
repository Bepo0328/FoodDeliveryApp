package kr.co.bepo.fooddeliveryapp.di

import kr.co.bepo.fooddeliveryapp.data.TestRestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLngEntity, get())
    }

    single<RestaurantRepository> { TestRestaurantRepository() }
}