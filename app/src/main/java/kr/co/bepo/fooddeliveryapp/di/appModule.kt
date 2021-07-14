package kr.co.bepo.fooddeliveryapp.di

import kotlinx.coroutines.Dispatchers
import kr.co.bepo.fooddeliveryapp.presentation.home.HomeViewModel
import kr.co.bepo.fooddeliveryapp.presentation.my.MyFragment
import kr.co.bepo.fooddeliveryapp.presentation.my.MyViewModel
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
    single { provideRetrofit(get(), get()) }
}

val domainModule = module {

}

val viewModule = module {
    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
}

val utilModule = module {
    single<ResourcesProvider> { DefaultResourcesProvider(androidApplication()) }
}