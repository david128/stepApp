package com.example.android.stepapp.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.*
import com.example.android.stepapp.pref.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HistoryViewModel(val dayDatabase: DayDatabaseDao,val goalDatabase: GoalDatabaseDao, application: Application): AndroidViewModel(application) {

    val allDays : LiveData<List<DayData>>

    var dts =DateToString()

    private var viewModelJob = Job()
    private val _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate

    var day : DayData? = null

    private  val pref  = Pref(application)
    val activeGoalName = pref.readActiveGoalfromDS.asLiveData()
    var activeGoalNameString = ""
    private var defaultGoal = GoalData()


    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        allDays = dayDatabase.getAllDays()
        _navigate.value=false
        //default goal for when no goals in system
        defaultGoal.stepGoal =1000
        defaultGoal.goalName= "Default Goal"
    }


    fun getDay(date:String)
    {
        var exist :Boolean = false
        viewModelScope.launch {
            exist = doesDayExist(date)
            //if day doesnt exist in db, create it
            if (!exist){
                val newDay = DayData()
                if (activeGoalNameString == ""){
                    newDay.stepGoalName = defaultGoal.goalName
                    newDay.stepGoal = defaultGoal.stepGoal
                    pref.saveActiveGoal(defaultGoal.goalName)
                }
                else{
                    newDay.stepGoalName = activeGoalNameString
                    newDay.stepGoal= getGoalByName(newDay.stepGoalName)?.stepGoal!!
                }
                newDay.stepDate = date
                insert(newDay)
                day= getThisDayFromDatabase(date)
            }
            else{
                //get this day
                day =getThisDayFromDatabase(date)
            }
            //now have day so can navigate
            _navigate.value=true
        }
    }

    //check if day exists
    private suspend fun doesDayExist(date:String): Boolean{
        return dayDatabase.doesDayExist(date)
    }

    //get from db using string date
    private suspend fun getThisDayFromDatabase(date:String): DayData?{
        return withContext(Dispatchers.IO){
            var day = dayDatabase.getSpecificDay(date)
            //do check here
            day
        }
    }

    fun deleteHistory(){
        viewModelScope.launch {
            clearHistory()
        }
    }

    private suspend fun clearHistory(){
        dayDatabase.clear()
    }


    private suspend fun getGoalByName(goalName: String) : GoalData?{
        return withContext(Dispatchers.IO){
            var goal = goalDatabase.getGoalByName(goalName)
            goal
        }
    }

    fun reseetNavigate(){
        _navigate.value=false
    }

    //insert to db
    private suspend fun  insert(day: DayData){
        withContext(Dispatchers.IO){
            dayDatabase.insert(day)
        }
    }

}