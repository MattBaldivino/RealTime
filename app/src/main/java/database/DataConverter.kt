package database

import android.util.Base64
import androidx.room.TypeConverter
import java.util.Date

class DataConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): String? {
        return byteArray?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    @TypeConverter
    fun toByteArray(string: String?): ByteArray? {
        return string?.let { Base64.decode(it, Base64.DEFAULT) }
    }
}