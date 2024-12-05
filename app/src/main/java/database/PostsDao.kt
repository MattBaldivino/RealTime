package database

import androidx.room.*
import com.bignerdranch.android.realtime.Posts
import java.util.Date

@Dao
interface PostsDao {
    //add post to db
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(posts: Posts): Long

    //get posts by date
    //can use this to display all posts from the current day
    @Query("SELECT * FROM posts WHERE date LIKE :date")
    fun getPosts(date: Int): List<Posts>

    //deleting all posts
    @Query("DELETE FROM Posts")
    fun deleteAll()
}