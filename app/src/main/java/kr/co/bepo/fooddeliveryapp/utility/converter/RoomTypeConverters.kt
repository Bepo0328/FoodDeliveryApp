package kr.co.bepo.fooddeliveryapp.utility.converter

import androidx.room.TypeConverter

object RoomTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toString(pair: Pair<Int, Int>): String =
        "${pair.first},${pair.second}"

    @TypeConverter
    @JvmStatic
    fun toIntPair(str: String): Pair<Int, Int> {
        val splitStr = str.split(",")
        return Pair(Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]))
    }
}