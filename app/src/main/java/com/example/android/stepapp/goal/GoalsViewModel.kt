package com.example.android.stepapp.goal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.*

class GoalsViewModel(val database: GoalDatabaseDao, application: Application): AndroidViewModel(application) {

    val readAllData : LiveData<List<GoalData>>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var deletedGoal : GoalData? = null

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        readAllData = database.getAllGoals()
    }

    fun deleteGoal(goal:GoalData){
        //store goal incase of undo
        deletedGoal= goal
        //delete goal
        viewModelScope.launch {
            database.deleteGoal(goal)
        }

    }

    fun restoreGoal()
    {
        //undo deletion
        if (deletedGoal!=null){
            var newGoal = GoalData()
            newGoal.stepGoal = deletedGoal!!.stepGoal
            newGoal.goalName = deletedGoal!!.goalName

            viewModelScope.launch {
                insert(newGoal)
            }
            deletedGoal=null
        }

    }

    private suspend fun  insert(goal: GoalData) {
        withContext(Dispatchers.IO) {
            database.insert(goal)
        }
    }
}
