package com.example.android.stepapp.addGoal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.*

class AddGoalViewModel( val database: GoalDatabaseDao, application: Application): AndroidViewModel(application){




    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private val _state =MutableLiveData<Int>()
    val state: LiveData<Int>
        get() = _state


    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }




    init {
        _state.value=0
    }

    fun attemptAddGoal(goalData : GoalData){
        viewModelScope.launch{
            if (doesGoalExist(goalData.goalName)) {
                _state.value=1
            }
            else{
                insert(goalData)
                _state.value=2
            }

        }
    }

    fun resetState(){
        _state.value=0
    }

    private suspend fun  insert(goal: GoalData) {
        withContext(Dispatchers.IO) {
            database.insert(goal)
        }
    }



    private suspend fun doesGoalExist(name:String): Boolean{
        return database.doesGoalExistWithName(name)
    }
}