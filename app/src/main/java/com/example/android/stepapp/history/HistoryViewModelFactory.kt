package com.example.android.stepapp.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.goal.GoalsViewModel
import java.lang.IllegalArgumentException

class HistoryViewModelFactory(private val dataSource: DayDatabaseDao, private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun<T: ViewModel?> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}