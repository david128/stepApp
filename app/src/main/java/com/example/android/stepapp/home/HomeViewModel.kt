package com.example.android.stepapp.home

import android.app.Application
import androidx.lifecycle.*
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.DayDatabaseDao
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel (val database: DayDatabaseDao, application: Application): AndroidViewModel(application) {


    private val _steps = MutableLiveData<Float>()
    val steps : LiveData<Float>
        get() = _steps

    private val _max = MutableLiveData<Float>()
    val max : LiveData<Float>
        get() = _max

    private var viewModelJob = Job()

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private var thisDay = MutableLiveData<DayData?>()
    private val days = database.getAllDays()
    //val dayString = Transformations.map(days){days -> formatDays(days,application.resources)    }

    init{
        _steps.value=0f;
        _max.value=1000f;
    }

    private fun initDay(){
        uiScope.launch {
            thisDay.value = getThisDayFromDatabase()
        }
    }

    private suspend fun getThisDayFromDatabase(): DayData?{
        return withContext(Dispatchers.IO){
            var day = database.getToday()
            //do check here
            day
        }
    }

    fun onStartTracking(){
        uiScope.launch {
            val newDay = DayData()
            insert(newDay)
            thisDay.value = getThisDayFromDatabase()
        }
    }

    private suspend fun  insert(day: DayData){
        withContext(Dispatchers.IO){
            database.insert(day)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            val oldDay = thisDay.value?: return@launch
            update(oldDay)
        }
    }

    private suspend fun update (day:DayData){
        withContext(Dispatchers.IO){
            database.update(day)
        }
    }


    fun addStep(){
        _steps.value = (steps.value)?.plus(1)
    }
}