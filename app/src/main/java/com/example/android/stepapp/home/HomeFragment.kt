package com.example.android.stepapp.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.databinding.FragmentHomeBinding



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

        //database set up
        val application = requireNotNull(this.activity).application

        val dataSource = DayDatabase.getInstance(application).dayDatabaseDao

        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        val dayViewModel= ViewModelProvider(this,viewModelFactory).get(HomeViewModel::class.java)

        binding.homeTrackerViewModel= dayViewModel
        binding.setLifecycleOwner (this)



        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //initial update


        binding.button2.setOnClickListener{viewModel.addStep()}
        binding.button3.setOnClickListener{viewModel.addStep()}
        binding.button4.setOnClickListener{viewModel.addStep()}

        viewModel.steps.observe(this, Observer {newSteps ->
            binding.circularProgressBar.progress=newSteps
        })
        viewModel.max.observe(this, Observer {newMax ->
            binding.circularProgressBar.progressMax=newMax
        })

        return binding.root
    }








}