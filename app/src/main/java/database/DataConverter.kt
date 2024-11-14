package database

import androidx.room.TypeConverter
import java.util.Date

class DataConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}