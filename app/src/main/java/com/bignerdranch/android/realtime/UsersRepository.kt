package com.bignerdranch.android.realtime
import database.UsersDao as UsersDao

class UsersRepository(private val usersDao: UsersDao){
    suspend fun insertUser(users: Users){
        usersDao.insertUser(users)
    }

    suspend fun getUser(username: String): Users {
        return usersDao.getUserDataDetails(username)
    }

    suspend fun deleteAll(){
        usersDao.deleteAll()
    }
}

