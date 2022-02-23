package com.example.android.stepapp.updateGoal

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
import androidx.navigation.fragment.navArgs
import com.example.android.stepapp.R
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentUpdateGoalBinding


class UpdateGoalFragment : Fragment() {


    private lateinit var viewModel: UpdateGoalViewModel
    private val args by navArgs<UpdateGoalFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding : FragmentUpdateGoalBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_update_goal, container, false)


        val application = requireNotNull(this.activity).application
        val dataSource = GoalDatabase.getInstance(application).goalDatabaseDao
        val viewModelFactory = UpdateGoalViewModelFactory(dataSource, application)
        viewModel= ViewModelProvider(this,viewModelFactory).get(UpdateGoalViewModel::class.java)


        binding.updateTextGoalName.setText( args.currentGoal.goalName)
        binding.updateTextNumber.setText(args.currentGoal.stepGoal.toString())

        binding.updateGoalButton.setOnClickListener{
            updateGoal(binding.updateTextGoalName.text.toString(), binding.updateTextNumber.text)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateGoal(name: String, steps: Editable){
        if (inputCheck(name,steps)){
            //create goal
            val updatedGoal = GoalData()
            updatedGoal.goalID = args.currentGoal.goalID
            updatedGoal.goalName = name
            updatedGoal.stepGoal = Integer.parseInt(steps.toString())

            //update current goal
            viewModel.onUpdateGoal(updatedGoal)
            findNavController().navigate(R.id.action_updateGoalFragment_to_goalsFragment)


        }

    }

    private fun inputCheck(name: String, steps: Editable):Boolean{
        return !(TextUtils.isEmpty(name) && steps.isEmpty())
    }


}