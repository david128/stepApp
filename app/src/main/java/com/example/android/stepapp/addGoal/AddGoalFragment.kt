package com.example.android.stepapp.addGoal

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.stepapp.R
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentAddGoalBinding


class AddGoalFragment : Fragment() {

    private lateinit var addGoalViewModel : AddGoalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentAddGoalBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_goal, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GoalDatabase.getInstance(application).goalDatabaseDao

        val viewModelFactory = AddGoalViewModelFactory(dataSource, application)

       addGoalViewModel= ViewModelProvider(this,viewModelFactory).get(AddGoalViewModel::class.java)

        binding.addGoalButton.setOnClickListener{
            insertDataToDatabase(binding.editTextGoalName.text.toString(), binding.editTextNumber.text)

        }

        return binding.root
    }

    private fun insertDataToDatabase(goalName: String, stepGoal: Editable){
        if (inputCheck(goalName,stepGoal))
        {
            val goal  = GoalData()
            goal.goalName =goalName
            goal.stepGoal = Integer.parseInt(stepGoal.toString())
            addGoalViewModel.addGoal(goal)
            //Now added, navigate back
            findNavController().navigate(R.id.action_addGoalFragment_to_goalsFragment)

        }
    }

    //check not empty
    private fun inputCheck(goalName: String, stepGoal: Editable): Boolean{
        return !(TextUtils.isEmpty(goalName) && stepGoal.isEmpty() )
    }
}