package com.example.android.stepapp.addGoal

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.stepapp.R
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentAddGoalBinding


class AddGoalFragment : Fragment() {

    private lateinit var viewModel : AddGoalViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentAddGoalBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_goal, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = GoalDatabase.getInstance(application).goalDatabaseDao

        val viewModelFactory = AddGoalViewModelFactory(dataSource, application)

       viewModel= ViewModelProvider(this,viewModelFactory).get(AddGoalViewModel::class.java)

        binding.addGoalButton.setOnClickListener{
            insertDataToDatabase(binding.editTextGoalName.text.toString(), binding.editTextNumber.text)
        }


        //observe state and act accordingly
        //1 -> Error
        //2 -> Correct so allow.
        viewModel.state.observe(viewLifecycleOwner, Observer { s ->
            when(s){
                1-> {
                    Toast.makeText(
                        requireContext(),
                        "Error goal already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.resetState()
                }
                2->findNavController().navigate(R.id.action_addGoalFragment_to_goalsFragment)
            }
        })

        return binding.root
    }

    private fun insertDataToDatabase(goalName: String, stepGoal: Editable){
        if (inputCheck(goalName,stepGoal))
        {
            val goal  = GoalData()
            goal.goalName =goalName
            goal.stepGoal = Integer.parseInt(stepGoal.toString())
            viewModel.attemptAddGoal(goal)
        }
    }

    //check not empty
    private fun inputCheck(goalName: String, stepGoal: Editable): Boolean{
        var flag = 0
        if (TextUtils.isEmpty(goalName)){
            flag++
        }
        if (stepGoal.isEmpty()){
            flag = flag + 2
        }

        when(flag){
            0-> return true
            1-> Toast.makeText(
                requireContext(),
                "Error: please enter a goal name",
                Toast.LENGTH_SHORT
            ).show()
            2->Toast.makeText(
                requireContext(),
                "Error: please enter a step goal value",
                Toast.LENGTH_SHORT
            ).show()
            3->Toast.makeText(
                requireContext(),
                "Error: please enter a step goal name and target",
                Toast.LENGTH_SHORT
            ).show()
        }
        return false
    }
}