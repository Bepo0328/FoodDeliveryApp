package kr.co.bepo.fooddeliveryapp.di

import kr.co.bepo.fooddeliveryapp.BuildConfig
import kr.co.bepo.fooddeliveryapp.data.api.FoodApiService
import kr.co.bepo.fooddeliveryapp.data.api.MapApiService
import kr.co.bepo.fooddeliveryapp.data.url.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideMapApiService(retrofit: Retrofit): MapApiService =
    retrofit.create(MapApiService::class.java)

fun provideFoodApiService(retrofit: Retrofit): FoodApiService =
    retrofit.create(FoodApiService::class.java)

fun provideMapRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit =
    Retrofit.Builder()
        .baseUrl(Url.TMAP_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

fun provideFoodRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
): Retrofit =
    Retrofit.Builder()
        .baseUrl(Url.FOOD_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

fun provideGsonConvertFactory(): GsonConverterFactory =
    GsonConverterFactory.create()

fun buildOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()

