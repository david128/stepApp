package com.example.android.stepapp.addGoal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.*

class AddGoalViewModel( val database: GoalDatabaseDao, application: Application): AndroidViewModel(application){




    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }


    init {

    }

    fun addGoal(goalData : GoalData){
        uiScope.launch{
            insert(goalData)
        }
    }

    private suspend fun  insert(goal: GoalData) {
        withContext(Dispatchers.IO) {
            database.insert(goal)
        }
    }
}