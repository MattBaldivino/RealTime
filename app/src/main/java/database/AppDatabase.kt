package database
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.realtime.Users
import com.bignerdranch.android.realtime.Posts
import database.UsersDao
import database.PostsDao

@Database(entities = [Users::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao():UsersDao
    abstract fun postsDao():PostsDao
}