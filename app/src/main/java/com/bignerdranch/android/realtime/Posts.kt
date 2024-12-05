package com.bignerdranch.android.realtime
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Posts (
    @PrimaryKey (autoGenerate = true)
    val id:Long=0,
    val photoFileName: String? = null,
    val date: Int,
    val owner: String? = null,
    val image: ByteArray? = null
)