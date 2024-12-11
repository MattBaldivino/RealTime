package com.bignerdranch.android.realtime

import android.content.Context
import java.util.Calendar

object FeedBoolean {

    private const val PREF_NAME = "feed_Boolean"
    private const val KEY_BOOLEAN = "isPictureTaken"
    private const val KEY_LAST_UPDATED_DATE = "lastUpdatedDate"

    fun getBoolean(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_BOOLEAN, false)
    }

    fun setBoolean(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_BOOLEAN, value).apply()
    }

    fun getLastUpdatedDate(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(KEY_LAST_UPDATED_DATE, 0)
    }

    fun setLastUpdatedDate(context: Context, timestamp: Long) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong(KEY_LAST_UPDATED_DATE, timestamp).apply()
    }

    fun resetBooleanIfNewDay(context: Context) {
        val lastUpdatedDate = getLastUpdatedDate(context)
        val currentDate = System.currentTimeMillis()

        // check day if it is different from previously saved day
        val lastUpdatedCalendar = Calendar.getInstance().apply { timeInMillis = lastUpdatedDate }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = currentDate }

        if (lastUpdatedCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR)) {
            // on a new day set boolean to false probably sync this up with the notification
            setBoolean(context, false)
        }

        // Update the last updated date
        setLastUpdatedDate(context, currentDate)
    }
}