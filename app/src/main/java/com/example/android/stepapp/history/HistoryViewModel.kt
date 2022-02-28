package com.example.android.stepapp.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


class HistoryViewModel(val database: DayDatabaseDao, application: Application): AndroidViewModel(application) {

    val allDays : LiveData<List<DayData>>

    var dts =DateToString()



    init {
        allDays = database.getAllDays()
    }





}