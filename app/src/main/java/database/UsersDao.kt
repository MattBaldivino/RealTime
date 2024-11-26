package database

import androidx.room.*
import com.bignerdranch.android.realtime.Users

@Dao
interface UsersDao {
    //add a user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: Users): Long

    //get user details
    @Query("SELECT * FROM users WHERE username LIKE :username")
    fun getUserDataDetails(username: String): Users

    //deleting all users
    @Query("DELETE FROM Users")
    fun deleteAll()

}
