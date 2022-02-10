package com.example.android.stepapp.goal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.android.stepapp.database.GoalDatabaseDao

import java.lang.IllegalArgumentException

class GoalsViewModelFactory(private val dataSource: GoalDatabaseDao, private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun<T: ViewModel?> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)){
            return GoalsViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}