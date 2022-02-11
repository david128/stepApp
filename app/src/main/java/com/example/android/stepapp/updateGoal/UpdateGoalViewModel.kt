package com.example.android.stepapp.updateGoal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.*

class UpdateGoalViewModel(val database: GoalDatabaseDao, application: Application): AndroidViewModel(application) {


    private var viewModelJob = Job()

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    private var goalId =0L
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var _thisGoal = MutableLiveData<GoalData?>()
    val thisGoal : LiveData<GoalData?>
        get() = _thisGoal


    fun setID( id:Long){
        goalId=id
        initGoal()
    }

    private fun initGoal(){
        uiScope.launch {
            _thisGoal.value = getThisGoalFromDatabase()
        }

    }

    private suspend fun getThisGoalFromDatabase(): GoalData?{
        return withContext(Dispatchers.IO){
            var goal = database.get(goalId)
            goal
        }
    }

    fun onUpdateGoal(goal: GoalData){
        uiScope.launch {
            updateGoal(goal)
        }
    }

    private suspend fun updateGoal(goal:GoalData){
        withContext(Dispatchers.IO) {
            database.update(goal)
        }
    }



}