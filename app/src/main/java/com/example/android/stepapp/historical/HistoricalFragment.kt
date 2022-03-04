package com.example.android.stepapp.historical

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.stepapp.R
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentHistoricalBinding
import com.example.android.stepapp.databinding.FragmentHistoryBinding
import com.example.android.stepapp.home.HomeViewModel
import com.example.android.stepapp.home.HomeViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [HistoricalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricalFragment : Fragment() {


    private val args : HistoricalFragmentArgs by navArgs()
    private lateinit var binding : FragmentHistoricalBinding
    private lateinit var viewModel : HistoricalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //database set up
        val application = requireNotNull(this.activity).application

        val dayDataSource = DayDatabase.getInstance(application).dayDatabaseDao
        val goalDataSource = GoalDatabase.getInstance(application).goalDatabaseDao

        val viewModelFactory = HistoricalViewModelFactory(dayDataSource,goalDataSource, application)

        viewModel= ViewModelProvider(this,viewModelFactory).get(HistoricalViewModel::class.java)
        //save day initial to update and change
        viewModel.loadDay(args.day)
        binding = DataBindingUtil.inflate( inflater,R.layout.fragment_historical, container, false)
        Log.d("args",args.day.stepDate )
        binding.historicalDateTextView.text = args.day.stepDate
        Log.d("args",args.day.stepGoal.toString() )
        binding.historicalTargetSteps.setText(args.day.stepGoal.toString())
        Log.d("args",args.day.stepGoalName )
        binding.goalHistoricalDropdown.setText( args.day.stepGoalName)
        Log.d("args",args.day.stepCount.toString() )
        binding.historicalStepCount.setText(args.day.stepCount.toString())

        viewModel.hPref.observe(this, { boolean ->
            binding.historicalUpdateButton.isEnabled=(boolean)
            binding.historicalStepCount.isEnabled = (boolean)

        })
        setupDropdown()

        binding.historicalUpdateButton.setOnClickListener {
            viewModel.updateDay(Integer.parseInt(binding.historicalStepCount.text.toString()),
                            binding.goalHistoricalDropdown.text.toString())
            findNavController().navigate(R.id.action_historical_to_historyFragment)
        }


        return binding.root
    }


    private fun setupDropdown(){


        val dropDown = binding.goalHistoricalDropdown
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

        dropDown.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Home", "Text changed" + dropDown.text.toString())
                viewModel.changeDayGoal(dropDown.text.toString())
                binding.historicalTargetSteps.setText(viewModel.day.stepGoal.toString())
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })




    }
}