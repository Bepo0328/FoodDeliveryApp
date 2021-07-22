package kr.co.bepo.fooddeliveryapp.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity
import kr.co.bepo.fooddeliveryapp.data.entity.MapSearchInfoEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantEntity
import kr.co.bepo.fooddeliveryapp.data.entity.RestaurantFoodEntity
import kr.co.bepo.fooddeliveryapp.data.preference.AppPreferenceManager
import kr.co.bepo.fooddeliveryapp.data.repository.map.DefaultMapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.map.MapRepository
import kr.co.bepo.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import kr.co.bepo.fooddeliveryapp.data.repository.order.OrderRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import kr.co.bepo.fooddeliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.DefaultUserRepository
import kr.co.bepo.fooddeliveryapp.data.repository.user.UserRepository
import kr.co.bepo.fooddeliveryapp.presentation.home.HomeViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantCategory
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.RestaurantListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.RestaurantDetailViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.menu.RestaurantMenuListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.home.restaurant.detail.review.RestaurantReviewListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.like.RestaurantLikeListViewModel
import kr.co.bepo.fooddeliveryapp.presentation.my.MyViewModel
import kr.co.bepo.fooddeliveryapp.presentation.myloaction.MyLocationViewModel
import kr.co.bepo.fooddeliveryapp.presentation.order.OrderMenuListViewModel
import kr.co.bepo.fooddeliveryapp.utility.event.MenuChangeEventBus
import kr.co.bepo.fooddeliveryapp.utility.provider.DefaultResourcesProvider
import kr.co.bepo.fooddeliveryapp.utility.provider.ResourcesProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    single { Firebase.firestore }
}

val dataModule = module {
    single { provideMapApiService(get(qualifier = named("map"))) }
    single(named("map")) { provideMapRetrofit(get(), get()) }

    single { provideFoodApiService(get(qualifier = named("food"))) }
    single(named("food")) { provideFoodRetrofit(get(), get()) }

    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }

    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    single { AppPreferenceManager(androidApplication()) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(), get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(), get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }

}

val domainModule = module {

}

val presentModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyViewModel(get()) }
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
    viewModel { (restaurantEntity: RestaurantEntity) ->
        RestaurantDetailViewModel(
            restaurantEntity,
            get(),
            get()
        )
    }
    viewModel { (restaurantId: Long, restaurantFoodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(
            restaurantId,
            restaurantFoodList,
            get()
        )
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { OrderMenuListViewModel(get(), get()) }
}

val utilModule = module {
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
    single { MenuChangeEventBus() }
}