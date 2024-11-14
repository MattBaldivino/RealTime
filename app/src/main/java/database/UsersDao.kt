package database

import androidx.room.*
import com.bignerdranch.android.realtime.Users

@Dao
interface UsersDao {
    //for single user insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: Users): Long

    //for list of users insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserAll(users: List<Users>): List<Long>

    //checking user exist or not in our db
    @Query("SELECT * FROM Users WHERE id LIKE :id AND password LIKE :password")
    fun readLoginData(id: Long, password: String):Users


    //getting user data details
    @Query("select * from users where username Like :username")
    fun getUserDataDetails(username: String):Users

    //deleting all user from db
    @Query("DELETE FROM Users")
    fun deleteAll()
}