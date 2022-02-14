package com.example.android.stepapp.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao


class HistoryViewModel(val database: DayDatabaseDao, application: Application): AndroidViewModel(application) {

    val readAllData : LiveData<List<DayData>>

    init {
        readAllData = database.getAllDays()

    }
}