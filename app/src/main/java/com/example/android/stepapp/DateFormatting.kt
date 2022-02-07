package com.example.android.stepapp

import java.text.SimpleDateFormat
import java.util.*

public abstract class DateFormatting {
    val sdf = SimpleDateFormat("dd/M/yyyy")

    fun getCurrentDate(): String {
        return sdf.format(Date())
    }
}