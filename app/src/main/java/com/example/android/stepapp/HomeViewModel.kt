package com.example.android.stepapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _steps = MutableLiveData<Float>()
    val steps : LiveData<Float>
        get() = _steps

    private val _max = MutableLiveData<Float>()
    val max : LiveData<Float>
        get() = _max



    init{
        _steps.value=0f;
        _max.value=1000f;
    }

    override fun onCleared() {
        super.onCleared()

    }

    fun addStep(){
        _steps.value = (steps.value)?.plus(1)
    }
}