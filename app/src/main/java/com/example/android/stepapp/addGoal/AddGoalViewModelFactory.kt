package com.example.android.stepapp.addGoal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.goal.GoalsViewModel
import java.lang.IllegalArgumentException

class AddGoalViewModelFactory(private val dataSource: GoalDatabaseDao, private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun<T: ViewModel?> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(AddGoalViewModel::class.java)){
            return AddGoalViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}