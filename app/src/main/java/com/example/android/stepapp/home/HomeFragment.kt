package com.example.android.stepapp.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.stepapp.DateToString
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var viewModel : HomeViewModel

    private lateinit var binding: FragmentHomeBinding

    override fun onResume() {
        super.onResume()
        setupDropdown()
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




        //update day
        binding.buttonPrev.setOnClickListener{

            viewModel.nextDay(-1)
            binding.topText.text = dts.toSimpleString(viewModel.currDate.time)
        }
        binding.buttonNext.setOnClickListener{

            viewModel.nextDay(+1)
            binding.topText.text = dts.toSimpleString(viewModel.currDate.time)
        }


        binding.addStepsButton.setOnClickListener{viewModel.addStep()}

        binding.addAmountEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()){
                    binding.addStepsButton.text = "+" + s.toString()
                    viewModel.changeAddAmount(Integer.parseInt(s.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        viewModel.thisDay.observe(this, Observer{newDay ->
            if (newDay != null) {
                binding.circularProgressBar.progress=newDay.stepCount.toFloat()
                binding.percentage.setText(getPercentage(newDay.stepCount.toFloat(),newDay.stepGoal.toFloat()).toString() + "%")
            }

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

    private fun setupDropdown(){

        val dropDown = binding.goalHomeDropdown
        var goalList= arrayListOf<String>()
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.goal_dropdown, goalList)
        dropDown.setAdapter(arrayAdapter)

        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->

            goalList.clear()
            for (g in goals) {
                goalList.add(g.goalName)
            }
            //notify updated list
            arrayAdapter.notifyDataSetChanged()
        })

        //when item selected, text changes,
        dropDown.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setSelectedGoal(dropDown.text.toString())
                Toast.makeText(requireContext(), dropDown.text.toString(), Toast.LENGTH_SHORT).show()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })


    }




}