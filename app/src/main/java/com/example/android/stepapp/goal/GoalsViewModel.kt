package com.example.android.stepapp.goal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GoalsViewModel(val database: GoalDatabaseDao, application: Application): AndroidViewModel(application) {

    val readAllData : LiveData<List<GoalData>>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        readAllData = database.getAllGoals()
    }

    fun deleteGoal(goal:GoalData){
        uiScope.launch {
            database.deleteGoal(goal)
        }


    }
}
