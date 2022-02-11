package com.example.android.stepapp.updateGoal

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.goal.GoalsViewModel
import java.lang.IllegalArgumentException

class UpdateGoalViewModelFactory(private val dataSource: GoalDatabaseDao, private val application: Application):
    ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun<T: ViewModel?> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(UpdateGoalViewModel::class.java)){
            return UpdateGoalViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}