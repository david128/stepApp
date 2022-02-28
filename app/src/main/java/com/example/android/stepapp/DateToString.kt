package com.example.android.stepapp

import java.text.SimpleDateFormat
import java.util.*

class DateToString {
    val format = SimpleDateFormat("dd/MM/yyy")
    fun toSimpleString(date: Date) : String {
        return format.format(date)
    }

    fun toDateFromString(date: String) : Date{
        return format.parse(date)
    }
}