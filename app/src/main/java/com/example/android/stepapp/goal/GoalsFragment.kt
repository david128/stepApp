package com.example.android.stepapp.goal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stepapp.R
import com.example.android.stepapp.addGoal.AddGoalFragment
import com.example.android.stepapp.database.DayDatabase
import com.example.android.stepapp.database.DayDatabaseDao
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentGoalsBinding
import com.example.android.stepapp.databinding.FragmentHomeBinding
import com.example.android.stepapp.updateGoal.UpdateGoalFragment
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 * Use the [GoalsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalsFragment : Fragment(), ListAdapter.OnGoalListner {


    private lateinit var viewModel: GoalsViewModel
    private var activeName = ""
    private var allowEditing = false

    override fun onResume() {
        super.onResume()
        viewModel.getThisDay()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?)
    : View? {

        // Inflate the layout for this fragment
        val binding : FragmentGoalsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goals, container, false)
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_goalsFragment2_to_addGoalFragment)
        }



        //db and vm set up
        val application = requireNotNull(this.activity).application
        val goalDataSource = GoalDatabase.getInstance(application).goalDatabaseDao
        val dayDataSource = DayDatabase.getInstance(application).dayDatabaseDao
        val viewModelFactory = GoalsViewModelFactory(goalDataSource,dayDataSource, application)
        viewModel= ViewModelProvider(this,viewModelFactory).get(GoalsViewModel::class.java)

        //recycler view set up
        val adapter = ListAdapter(this, "e")
        val recyclerView = binding.recycleGoalView
        recyclerView.adapter= adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        //delete functionality
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                //remove item
                viewModel.deleteGoal(adapter.getItem(pos))
                recyclerView.adapter?.notifyItemRemoved(pos)
                val snack = view?.let { Snackbar.make(it,"Item Deleted",Snackbar.LENGTH_LONG) }
                if (snack != null) {
                    
                    snack.setAction("Undo", View.OnClickListener {
                        viewModel.restoreGoal()
                    })
                    snack.show()
                }

            }
        }

        //touch helper for swipe to delete
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        //observe goal data and update on change
        viewModel.readAllData.observe(viewLifecycleOwner, Observer { goal ->
           updateGoals(goal,adapter,binding.activeGoalName,binding.activeGoalTarget)
        })

        //observe thisDay and update active
        viewModel.activeGoalName.observe(viewLifecycleOwner, Observer { day ->
            if (day != null) {
                Log.d("acG","change in ActiveGoalDetected ")
                activeName=day
                //refresh goal list
                viewModel.getGoals()
                //update goal data
                viewModel.readAllData.value?.let { updateGoals(it,adapter,binding.activeGoalName,binding.activeGoalTarget ) }
            }
        })

        viewModel.gPref.observe(this, { boolean ->
            allowEditing = boolean  })

        return binding.root
    }

    private fun updateGoals(goals: List<GoalData>, adapter: ListAdapter, nameTV: TextView, targetTV: TextView){
        Log.d("acG","change in GoalData Detected ")
        var inactiveGoals = goals as ArrayList<GoalData>
        var activeGoal = GoalData()
        for (g in inactiveGoals){
            if (g.goalName==activeName){
                activeGoal =g
            }
        }
        inactiveGoals.remove(activeGoal)
        nameTV.text = activeGoal.goalName
        targetTV.text = activeGoal.stepGoal.toString()
        adapter.setData(inactiveGoals)
    }


    override fun onGoalClick(goal : GoalData) {
        if (allowEditing == true){
            val action = GoalsFragmentDirections.actionGoalsFragment2ToUpdateGoalFragment(goal)
            findNavController().navigate(action)
        }else {
            Toast.makeText(requireContext(), "Editing Disabled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGoalLongClick(name: String) {


    }


}