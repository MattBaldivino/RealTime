package com.bignerdranch.android.realtime
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import com.bignerdranch.android.realtime.Users

@Entity
data class Posts (
    @PrimaryKey (autoGenerate = true)
    val id:Long=0,
    val photoFileName: String? = null,
    val date: Date,
    val users: Users
)