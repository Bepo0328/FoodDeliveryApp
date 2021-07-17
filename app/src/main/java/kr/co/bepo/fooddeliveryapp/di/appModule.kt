package kr.co.bepo.fooddeliveryapp.di

import kotlinx.coroutines.Dispatchers
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.data.repository.map.DefaultMapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.map.MapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.presentation.home.HomeViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListViewModel
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
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single { provideMapRetrofit(get(), get()) }
    single { provideMapApiService(get()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
}

val domainModule = module {

}

val presentModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) ->
        RestaurantListViewModel(
            restaurantCategory,
            get()
        )
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) ->
        MyLocationViewModel(
            mapSearchInfoEntity,
            get()
        )
    }
}

val utilModule = module {
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
}