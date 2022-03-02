package com.example.android.stepapp.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.applikeysolutions.cosmocalendar.model.Day
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.database.DayData
import com.example.android.stepapp.database.DayDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HistoryViewModel(val database: DayDatabaseDao, application: Application): AndroidViewModel(application) {

    val allDays : LiveData<List<DayData>>

    var dts =DateToString()

    private var viewModelJob = Job()
    private val _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate

    var day : DayData? = null


    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        allDays = database.getAllDays()
        _navigate.value=false
    }


    fun getDay(date:String)
    {
        var exist :Boolean = false
        viewModelScope.launch {
            exist = doesDayExist(date)
            //if day doesnt exist in db, create it
            if (!exist){
                val newDay = DayData()
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
        return database.doesDayExist(date)
    }

    //get from db using string date
    private suspend fun getThisDayFromDatabase(date:String): DayData?{
        return withContext(Dispatchers.IO){
            var day = database.getSpecificDay(date)
            //do check here
            day
        }
    }

    fun reseetNavigate(){
        _navigate.value=false
    }

    //insert to db
    private suspend fun  insert(day: DayData){
        withContext(Dispatchers.IO){
            database.insert(day)
        }
    }

}