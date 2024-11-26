package com.bignerdranch.android.realtime

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users (
    @PrimaryKey (autoGenerate = true)
    val id:Long=0,
    var username: String?="",
    var password: String?=""
)