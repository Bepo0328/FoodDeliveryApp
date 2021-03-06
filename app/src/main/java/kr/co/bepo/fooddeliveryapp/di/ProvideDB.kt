package kr.co.bepo.fooddeliveryapp.di

import android.content.Context
import androidx.room.Room
import kr.co.bepo.fooddeliveryapp.data.db.ApplicationDatabase

fun provideDB(context: Context): ApplicationDatabase =
    Room.databaseBuilder(context, ApplicationDatabase::class.java, ApplicationDatabase.DB_NAME).build()

fun provideLocationDao(database: ApplicationDatabase) =
    database.LocationDao()

fun provideRestaurantDao(database: ApplicationDatabase) =
    database.RestaurantDao()

fun provideFoodMenuBasketDao(database: ApplicationDatabase) =
    database.FoodMenuBasketDao()