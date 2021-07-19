package kr.co.bepo.fooddeliveryapp.di

import kotlinx.coroutines.Dispatchers
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.repository.map.DefaultMapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.map.MapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.DefaultUserRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.UserRepository
import kr.co.bepo.fooddeliveryapp.presentation.home.HomeViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.RestaurantDetailViewModel
import kr.co.bepo.fooddeliveryapp.presentation.my.MyViewModel
import kr.co.bepo.fooddeliveryapp.presentation.myloaction.MyLocationViewModel
import kr.co.bepo.fooddeliveryapp.utility.provider.DefaultResourcesProvider
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
    single { Dispatchers.Main }
}

val dataModule = module {
    single { provideMapApiService(get()) }
    single { provideMapRetrofit(get(), get()) }
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
}

val domainModule = module {

}

val presentModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(
            restaurantCategory,
            locationLatLng,
            get()
        )
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(
            mapSearchInfoEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantEntity: RestaurantEntity) -> RestaurantDetailViewModel(restaurantEntity, get()) }
}

val utilModule = module {
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
}