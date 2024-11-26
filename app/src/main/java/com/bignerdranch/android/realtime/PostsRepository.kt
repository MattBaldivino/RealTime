package com.bignerdranch.android.realtime
import java.util.Date
import database.PostsDao as PostsDao

class PostsRepository(private val postsDao: PostsDao) {
    suspend fun insertPost(posts: Posts){
        postsDao.insertPost(posts)
    }

    //if List does not show all posts, try using Flow
    suspend fun getPosts(date: Date): List<Posts> {
        return postsDao.getPosts(date)
    }

    suspend fun deleteAll(){
        postsDao.deleteAll()
    }
}