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
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.R
import com.example.android.stepapp.addGoal.AddGoalFragment
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentGoalsBinding
import com.example.android.stepapp.databinding.FragmentUpdateGoalBinding
import com.example.android.stepapp.goal.GoalsViewModel
import com.example.android.stepapp.goal.GoalsViewModelFactory


class UpdateGoalFragment : Fragment() {


    private lateinit var viewModel: UpdateGoalViewModel

    fun newInstance(id:Long, name: String, steps: Int): UpdateGoalFragment? {
        val fragment = UpdateGoalFragment()
        val args = Bundle()
        args.putLong("id", id)
        args.putString("name", name)
        args.putInt("steps", steps)


        fragment.setArguments(args)
        return fragment
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        val binding : FragmentUpdateGoalBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_update_goal, container, false)



        val application = requireNotNull(this.activity).application
        val dataSource = GoalDatabase.getInstance(application).goalDatabaseDao
        val viewModelFactory = UpdateGoalViewModelFactory(dataSource, application)
        viewModel= ViewModelProvider(this,viewModelFactory).get(UpdateGoalViewModel::class.java)
        viewModel.setID(arguments!!.getLong("id", 0))

        binding.updateTextGoalName.setText( arguments!!.getString("name", ""))
        binding.updateTextNumber.setText(arguments!!.getInt("steps", 0).toString())

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
            updatedGoal.goalID = arguments!!.getLong("id", 0L)
            updatedGoal.GoalName = name
            updatedGoal.stepGoal = Integer.parseInt(steps.toString())
            Toast.makeText(requireContext(),"Updated", Toast.LENGTH_LONG).show()
            //update current goal
            viewModel.onUpdateGoal(updatedGoal)

        }

    }

    private fun inputCheck(name: String, steps: Editable):Boolean{
        return !(TextUtils.isEmpty(name) && steps.isEmpty())
    }


}