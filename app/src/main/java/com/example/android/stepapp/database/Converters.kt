package com.example.android.stepapp.database

import androidx.room.TypeConverter
import java.util.*

class Converters {
    //
    @TypeConverter
    open fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return if (date == null)null else date.getTime()
    }
}