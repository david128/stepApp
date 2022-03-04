package com.example.android.stepapp.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import com.example.android.stepapp.pref.Pref
import kotlinx.coroutines.*
import java.util.*

class HomeViewModel (val dayDatabaseDao: DayDatabaseDao,val goalDatabaseDao: GoalDatabaseDao, application: Application): AndroidViewModel(application) {


    private var selectedGoalName  =""
    private val liveTime = Calendar.getInstance()

    private  val pref  = Pref(application)
    val activeGoalName = pref.readActiveGoalfromDS.asLiveData()

    private val _steps = MutableLiveData<Float>()
    val steps : LiveData<Float>
        get() = _steps

    private val _addAmount = MutableLiveData<Int>()
    val addAmount : LiveData<Int>
        get() = _addAmount

    private val _max = MutableLiveData<Int>()
    val max : LiveData<Int>
        get() = _max

    val currDate = Calendar.getInstance()
    val dts = DateToString()

    val allGoals : LiveData<List<GoalData>>
    private var defaultGoal = GoalData()
    private var viewModelJob = Job()

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var _thisDay = MutableLiveData<DayData?>()
    val thisDay : LiveData<DayData?>
        get() = _thisDay
    //private val days = dayDatabaseDao.getAllDays()
    //val dayString = Transformations.map(days){days -> formatDays(days,application.resources)    }

    init{
        allGoals = goalDatabaseDao.getAllGoals()
        _steps.value=0f;

        //default goal for when no goals in system
        defaultGoal.stepGoal =1000
        defaultGoal.goalName= "Default Goal"

    }


    fun initDay(){
        //check if this day is in the db
        _addAmount.value= 100
        Log.d("setDbg", "initing new day")
        var exist :Boolean = false
        viewModelScope.launch {
            exist = doesDayExist()

            //if day doesnt exist in db, create it
            if (!exist)
            {
                Log.d("setDbg", "newDay doesnt exist")
                onNewDay()
            }
            else{
                //get this day
                _thisDay.value = getThisDayFromDatabase()
                Log.d("setDbg", "newDay does exist :" + (_thisDay.value?.stepDate ?: "missingDate") )
                newDayChangeGoal()
            }

        }

    }

    //function to check if given date is in db
    private suspend fun doesDayExist(): Boolean{
        return dayDatabaseDao.doesDayExist(dts.toSimpleString(currDate.time))
    }

    private suspend fun getThisDayFromDatabase(): DayData?{
        return withContext(Dispatchers.IO){
            var day = dayDatabaseDao.getSpecificDay(dts.toSimpleString(currDate.time))
            //do check here
            day
        }
    }

    //add to db
    private fun onNewDay(){
        val newDay = DayData()



        Log.d("setDbg", "creating new day " +dts.toSimpleString(currDate.time))
        newDay.stepDate = dts.toSimpleString(currDate.time)
        viewModelScope.launch {

            if (activeGoalName.value == "" ||  activeGoalName.value == null){
                newDay.stepGoalName = defaultGoal.goalName
                newDay.stepGoal = defaultGoal.stepGoal
                pref.saveActiveGoal(defaultGoal.goalName)
            }
            else{
                newDay.stepGoalName = activeGoalName.value!!
                newDay.stepGoal= getGoalByName(newDay.stepGoalName)?.stepGoal!!
            }

            insert(newDay)
            _thisDay.value= getThisDayFromDatabase()
            Log.d("setDbg", "fetching new day " +_thisDay.value.toString())
            newDayChangeGoal()
        }

    }

    private suspend fun  insert(day: DayData){
        withContext(Dispatchers.IO){
            dayDatabaseDao.insert(day)
        }
    }

    //stop tracking this day
    private fun onUpdateDay(day : DayData){
        viewModelScope.launch {
            update(day)
        }
    }

    private suspend fun update (day:DayData){
        withContext(Dispatchers.IO){
            dayDatabaseDao.update(day)
        }
    }


    fun addDefaultGoal(){

        viewModelScope.launch {
            addGoal(defaultGoal)
        }
    }

    private suspend fun addGoal (goal:GoalData){
        withContext(Dispatchers.IO){
            goalDatabaseDao.insert(goal)
        }
    }

    fun addStep(){
        //_steps.value = (steps.value)?.plus(_(addAmount.value!!))
        //_thisDay.value?.stepCount = thisDay.value?.stepCount?.plus((addAmount.value!!))!!
        val updatedDay = DayData()

        updatedDay.stepCount = _thisDay.value?.stepCount?.plus((addAmount.value!!))!!
        updatedDay.dayID= _thisDay.value!!.dayID
        updatedDay.stepDate= _thisDay.value!!.stepDate
        updatedDay.stepGoal= _thisDay.value!!.stepGoal
        updatedDay.stepGoalName= _thisDay.value!!.stepGoalName

        _thisDay.value = updatedDay
        onUpdateDay(updatedDay)


    }


    private suspend fun getGoalByName(goalName : String) : GoalData?{
        return withContext(Dispatchers.IO){
            var goal = goalDatabaseDao.getGoalByName(goalName)
            goal
        }
    }

    fun newDayChangeGoal(){
        Log.d("setDbg", "newDayChangeGoal ")
        if (_thisDay.value != null){

            if (_thisDay.value!!.stepGoalName =="" ){
                //no associated goal so assign previous one
                setDayGoal(selectedGoalName)
            }
            else{
                //change text to appropriate
                Log.d("setDbg", "day " + _thisDay.value.toString()+" already had a goal name ")
            }
        }
        else{
            Log.d("setDbg", "day was null ")
        }
    }

    fun changeDayGoal(goalName: String){
        //calls setDayGoal
        setDayGoal(goalName)


    }



    private fun setDayGoal(goalName: String){

         //get goal and this day and apply goal

        Log.d("setDbg", "setting" + _thisDay.value.toString()+ " to " + goalName)

        //check if need to update
        if (goalName != _thisDay.value?.stepGoalName ?: ""){
            viewModelScope.launch {


                var goal : GoalData? = getGoalByName(goalName)
                val updatedDay = DayData()

                if (_thisDay.value != null && goal != null) {

                    updatedDay.dayID = _thisDay.value!!.dayID
                    updatedDay.stepCount = _thisDay.value!!.stepCount
                    updatedDay.stepDate = _thisDay.value!!.stepDate
                    //update the day with goal selected
                    updatedDay.stepGoal = goal!!.stepGoal
                    _max.value = updatedDay.stepGoal
                    updatedDay.stepGoalName = goal!!.goalName
                    update(updatedDay)
                    _thisDay.value = updatedDay
                    //store this goal as active goal
                    pref.saveActiveGoal(_thisDay.value!!.stepGoalName)


                }

                Log.d("setDbg", "day goal set, updated now " + _thisDay.value.toString())
            }
        }






    }

    fun changeAddAmount(newAmount : Int){
        _addAmount.value= newAmount
    }

}