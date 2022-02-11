package com.example.android.stepapp.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalDatabaseDao
import java.lang.IllegalArgumentException


class HomeViewModelFactory(private val dayDataSource: DayDatabaseDao, private val goalDataSource: GoalDatabaseDao, private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun<T: ViewModel?> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(dayDataSource,goalDataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}