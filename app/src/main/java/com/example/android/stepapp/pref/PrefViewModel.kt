package com.example.android.stepapp.pref

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrefViewModel(application: Application) : AndroidViewModel(application) {
    private  val pref  = Pref(application)
    val hPref = pref.readHistoricalFromDS.asLiveData()
    val gPref = pref.readGoalEditingFromDS.asLiveData()
    fun saveHistoricalToDS(boolean: Boolean)= viewModelScope.launch(Dispatchers.IO) {
        pref.saveHistoricalToDS(boolean)
    }
    fun saveGoalEditingToDS(boolean: Boolean)= viewModelScope.launch(Dispatchers.IO) {
        pref.saveGoalEditingToDS(boolean)
    }
}