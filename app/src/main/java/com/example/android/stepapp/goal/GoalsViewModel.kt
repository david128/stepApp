package com.example.android.stepapp.goal

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.pref.Pref
import kotlinx.coroutines.*
import java.util.*

class GoalsViewModel(val goalDatabaseDao: GoalDatabaseDao, val dayDatabaseDao: DayDatabaseDao, application: Application): AndroidViewModel(application) {

    var readAllData : LiveData<List<GoalData>>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var deletedGoal : GoalData? = null
    private  val pref  = Pref(application)
    val gPref = pref.readGoalEditingFromDS.asLiveData()
    val activeGoalName = pref.readActiveGoalfromDS.asLiveData()

    //used to get current day, and then active goal
    private val currDate = Calendar.getInstance()
    private val dts = DateToString()


    private var _thisDay = MutableLiveData<DayData?>()


    private var _activeGoal = MutableLiveData<String?>()
    val activeGoal : LiveData<String?>
        get() = _activeGoal

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        readAllData = goalDatabaseDao.getAllGoals()
        getThisDay()
        Log.d("acG","Initiating")
    }



    fun getGoals(){
        readAllData = goalDatabaseDao.getAllGoals()
        Log.d("acG","Getting goals")
    }

    fun deleteGoal(goal:GoalData){
        //store goal incase of undo
        deletedGoal= goal
        //delete goal
        viewModelScope.launch {
            goalDatabaseDao.deleteGoal(goal)
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

    fun getThisDay(){
        viewModelScope.launch {
            _thisDay.value = getThisDayFromDatabase()
            Log.d("acG","got " + _thisDay.value)
            _activeGoal.value= _thisDay.value?.stepGoalName
            Log.d("acG","activeGoalNow " + activeGoal.value)
        }
    }


    private suspend fun  insert(goal: GoalData) {
        withContext(Dispatchers.IO) {
            goalDatabaseDao.insert(goal)
        }
    }

    private suspend fun getThisDayFromDatabase(): DayData?{
        return withContext(Dispatchers.IO){
            var day = dayDatabaseDao.getSpecificDay(dts.toSimpleString(currDate.time))
            //do check here
            day
        }
    }
}
