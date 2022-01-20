package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferrences {

    companion object {

        private val PREFERENCES_TIME = "preferences_time"
        private var sharedPreferrences: SharedPreferences? = null

        @Volatile
        private var instance: CustomSharedPreferrences? = null

        private val lock = Any()
        operator fun invoke(context: Context): CustomSharedPreferrences = instance ?: synchronized(lock) {
                instance ?: makeCustomSharedPreferences(context).also {
                    instance = it
                }
            }

        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferrences {
            sharedPreferrences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferrences()
        }

    }

    fun saveTime(time: Long) {
        sharedPreferrences?.edit(commit = true) {
            putLong(PREFERENCES_TIME, time)
        }
    }

    fun getTime() = sharedPreferrences?.getLong(PREFERENCES_TIME, 0)


}