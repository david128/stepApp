package com.example.android.stepapp.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*






class HomeFragment : Fragment() {

    private lateinit var viewModel : HomeViewModel

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        val dts = DateToString()

        //database set up
        val application = requireNotNull(this.activity).application

        val dayDataSource = DayDatabase.getInstance(application).dayDatabaseDao
        val goalDataSource = GoalDatabase.getInstance(application).goalDatabaseDao

        val viewModelFactory = HomeViewModelFactory(dayDataSource,goalDataSource, application)

        val dayViewModel= ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)

        binding.homeTrackerViewModel= dayViewModel
        binding.setLifecycleOwner (this)




        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //initial update

        //update day
        binding.buttonPrev.setOnClickListener{
            viewModel.onNewDay()
            viewModel.nextDay(-1)
            binding.topText.text = dts.toSimpleString(viewModel.currDate.time)
        }
        binding.buttonNext.setOnClickListener{
            viewModel.onNewDay()
            viewModel.nextDay(+1)
            binding.topText.text = dts.toSimpleString(viewModel.currDate.time)
        }


        binding.button4.setOnClickListener{viewModel.addStep()}


        viewModel.steps.observe(this, Observer {newSteps ->
            binding.circularProgressBar.progress=newSteps
            binding.percentage.setText(getPercentage(newSteps,(viewModel.max.value ?: 0.0f)).toString() + "%")

        })
        viewModel.max.observe(this, Observer {newMax ->
            binding.circularProgressBar.progressMax=newMax
            binding.percentage.setText(getPercentage((viewModel.steps.value ?: 0.0f), newMax).toString() + "%")
        })





        return binding.root
    }


    fun getPercentage(steps: Float, max: Float) : Int{
        return (steps/max *100f).toInt()
    }






}