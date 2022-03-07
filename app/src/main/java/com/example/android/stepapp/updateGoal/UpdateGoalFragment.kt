package com.example.android.stepapp.updateGoal

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
                2->findNavController().navigate(R.id.action_updateGoalFragment_to_goalsFragment)
            }
        })

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
            viewModel.attemptUpdate(updatedGoal)
            
        }

    }

    private fun inputCheck(name: String, steps: Editable):Boolean{
        var flag = 0
        if (TextUtils.isEmpty(name)){
            flag++
        }
        if (steps.isEmpty()){
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