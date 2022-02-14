package com.example.android.stepapp.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabaseDao
import kotlinx.coroutines.*
import java.util.*

class HomeViewModel (val dayDatabaseDao: DayDatabaseDao,val goalDatabaseDao: GoalDatabaseDao, application: Application): AndroidViewModel(application) {


    private val _steps = MutableLiveData<Float>()
    val steps : LiveData<Float>
        get() = _steps

    private val _max = MutableLiveData<Float>()
    val max : LiveData<Float>
        get() = _max

    val currDate = Calendar.getInstance()
    val dts = DateToString()

    val allGoals : LiveData<List<GoalData>>

    private var viewModelJob = Job()

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var thisDay = MutableLiveData<DayData?>()
    private val days = dayDatabaseDao.getAllDays()
    //val dayString = Transformations.map(days){days -> formatDays(days,application.resources)    }

    init{
        _steps.value=0f;
        _max.value=1000f;
        allGoals = goalDatabaseDao.getAllGoals()
        initDay()

    }


    fun nextDay( changeDate: Int){
        currDate.add(Calendar.DATE, changeDate)
        initDay()
    }

    private fun initDay(){
        //check if this day is in the db
        var exist :Boolean = false
/*        uiScope.launch {
            thisDay.value = getThisDayFromDatabase()
        }*/
        viewModelScope.launch {
            exist = doesDayExist()
        }
        if (!exist)
        {
            onNewDay()
        }


    }

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
    fun onNewDay(){
        val newDay = DayData()
        newDay.stepDate = dts.toSimpleString(currDate.time)
        uiScope.launch {

            insert(newDay)
        }
    }

    private suspend fun  insert(day: DayData){
        withContext(Dispatchers.IO){
            dayDatabaseDao.insert(day)
        }
    }

    //stop tracking this day
    fun onUpdateSteps(){
        uiScope.launch {

            val oldDay = thisDay.value?: return@launch
            update(oldDay)
        }
    }

    private suspend fun update (day:DayData){
        withContext(Dispatchers.IO){
            dayDatabaseDao.update(day)
        }
    }


    fun addStep(){
        _steps.value = (steps.value)?.plus(1)
    }
}