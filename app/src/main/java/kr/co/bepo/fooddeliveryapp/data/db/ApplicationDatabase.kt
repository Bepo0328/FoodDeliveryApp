package kr.co.bepo.fooddeliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.bepo.fooddeliveryapp.data.db.dao.LocationDao
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }

    abstract fun LocationDao(): LocationDao

}