package com.example.android.stepapp

import java.text.SimpleDateFormat
import java.util.*

class DateToString {
    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }
}