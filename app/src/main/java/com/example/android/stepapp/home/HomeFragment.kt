package com.example.android.stepapp.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var viewModel : HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onResume() {
        super.onResume()
        viewModel.allGoals.value?.let { setupDropdown(it) }
        //refresh day
        viewModel.initDay()
    }

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
        binding.topText.text = dts.toSimpleString(viewModel.currDate.time)


        binding.addStepsButton.setOnClickListener{viewModel.addStep()}

        binding.addAmountEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()){
                    binding.addStepsButton.text = "+" + s.toString()
                    viewModel.changeAddAmount(Integer.parseInt(s.toString()))
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.activeGoalName.observe(this, Observer{ name ->
            binding.goalHomeDropdown.setText(name)
        })

        viewModel.thisDay.observe(this, Observer{newDay ->
            if (newDay != null) {
                Log.d("Home", "thisDayObserve()" + newDay.toString())
                binding.circularProgressBar.progress=newDay.stepCount.toFloat()
                binding.circularProgressBar.progressMax=newDay.stepGoal.toFloat()
                binding.percentage.setText(getPercentage(newDay.stepCount,newDay.stepGoal).toString() + "%")
                binding.stepsTextView.setText(newDay.stepCount.toString())
                binding.homeGoalTextView.setText(newDay.stepGoal.toString())
                binding.goalHomeDropdown.setText(newDay.stepGoalName)
                viewModel.allGoals.value?.let { setupDropdown(it) }

            }

        })


        viewModel.max.observe(this, Observer {newMax ->
            Log.d("Home", "newMax.Observe()" + newMax.toString())
            binding.circularProgressBar.progressMax=newMax.toFloat()
            if(viewModel.thisDay.value?.stepCount!= null){
                binding.percentage.setText(getPercentage((viewModel.thisDay.value?.stepCount!!), newMax).toString() + "%")
            }
            binding.homeGoalTextView.setText(newMax.toString())
        })

        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            //update list
            if(goals.isEmpty()){
                Toast.makeText(requireContext(),"Empty",Toast.LENGTH_SHORT ).show()
                viewModel.addDefaultGoal()
            }
            setupDropdown(goals)
        })


        return binding.root
    }


    private fun getPercentage(steps: Int, max: Int) : Int{
        return (steps.toFloat()/max.toFloat() *100f).toInt()
    }

    private fun setupDropdown(goals : List<GoalData>){

        val dropDown = binding.goalHomeDropdown
        var goalList= arrayListOf<String>()
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.goal_dropdown, goalList)


        //update list
        populateList(goalList,goals)

        dropDown.setAdapter(arrayAdapter)
        arrayAdapter.notifyDataSetChanged()
        //when item selected, text changes,
        dropDown.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Home", "Text changed" + dropDown.text.toString())
                viewModel.changeDayGoal(dropDown.text.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
        })


    }

    private fun populateList(goalList: ArrayList<String>,goals : List<GoalData> ){
        goalList.clear()
        for (g in goals) {
            goalList.add(g.goalName)
        }
    }




}