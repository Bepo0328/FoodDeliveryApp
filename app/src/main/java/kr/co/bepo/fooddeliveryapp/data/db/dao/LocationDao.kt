package kr.co.bepo.fooddeliveryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kr.co.bepo.fooddeliveryapp.data.entity.LocationLatLngEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationLatLngEntity Where id=:id")
    suspend fun get(id: Long): LocationLatLngEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationLatLng: LocationLatLngEntity)

    @Query("DELETE FROM LocationLatLngEntity WHERE id=:id")
    suspend fun delete(id: Long)
}