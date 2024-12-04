package database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.realtime.Users
import com.bignerdranch.android.realtime.Posts

@Database(entities = [Users::class, Posts::class], version = 2, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao():UsersDao
    abstract fun postsDao():PostsDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add the new 'image' column to the Posts table (nullable for backward compatibility)
                db.execSQL("ALTER TABLE Posts ADD COLUMN image BLOB")
            }
        }

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).allowMainThreadQueries()
                    .addMigrations(migration_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}