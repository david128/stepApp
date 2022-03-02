package com.example.android.stepapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.pref.PrefViewModel
import android.widget.CompoundButton




class SettingsActivity : AppCompatActivity() {

    private lateinit var prefViewModel : PrefViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val hSwitch = findViewById<Switch>(R.id.historical_switch)
        val gSwitch = findViewById<Switch>(R.id.goal_editing_switch)

        prefViewModel = ViewModelProvider(this).get(PrefViewModel::class.java)

        //observe
        prefViewModel.hPref.observe(this,{data ->
            hSwitch.isChecked = data
        })
        //observe
        prefViewModel.gPref.observe(this,{data ->
            gSwitch.isChecked = data
        })


        //update pref
        hSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefViewModel.saveHistoricalToDS(isChecked)
        })
        //update pref
        gSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            prefViewModel.saveGoalEditingToDS(isChecked)
        })
    }
}
