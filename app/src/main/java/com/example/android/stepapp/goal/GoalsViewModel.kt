package com.example.android.stepapp.goal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao

class GoalsViewModel(val database: GoalDatabaseDao, application: Application): AndroidViewModel(application) {

    val readAllData : LiveData<List<GoalData>>

    init {
        readAllData = database.getAllGoals()
    }
}