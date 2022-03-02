package com.example.android.stepapp.historical

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.pref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoricalViewModel(val dayDatabaseDao: DayDatabaseDao, val goalDatabaseDao: GoalDatabaseDao, application: Application): AndroidViewModel(application)  {

    val allGoals : LiveData<List<GoalData>>
    var day = DayData()
    private  val pref  = Pref(application)
    val hPref = pref.readHistoricalFromDS.asLiveData()

    init {
        allGoals = goalDatabaseDao.getAllGoals()
    }

    fun loadDay(currentDay : DayData)
    {
        day=currentDay
    }

    private suspend  fun update(day : DayData){
        withContext(Dispatchers.IO){
            dayDatabaseDao.update(day)
        }
    }

    fun updateDay(stepCount:Int, stepGoal: Int, stepGoalName: String){
        day.stepGoal = stepGoal
        day.stepGoalName = stepGoalName
        day.stepCount = stepCount
        viewModelScope.launch {
            update(day)
        }
    }

}