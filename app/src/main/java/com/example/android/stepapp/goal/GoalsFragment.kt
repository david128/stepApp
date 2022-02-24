package com.example.android.stepapp.goal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val binding : FragmentGoalsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goals, container, false)
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_goalsFragment2_to_addGoalFragment)

        }





        val application = requireNotNull(this.activity).application
        val dataSource = GoalDatabase.getInstance(application).goalDatabaseDao
        val viewModelFactory = GoalsViewModelFactory(dataSource, application)
        viewModel= ViewModelProvider(this,viewModelFactory).get(GoalsViewModel::class.java)
        val adapter = ListAdapter(this)
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

        viewModel.readAllData.observe(viewLifecycleOwner, Observer { goal ->
            adapter.setData(goal)
        })


        return binding.root
    }

    override fun onGoalClick(goal : GoalData) {

        val action = GoalsFragmentDirections.actionGoalsFragment2ToUpdateGoalFragment(goal)
        findNavController().navigate(action)

    }

    override fun onGoalLongClick(name: String) {
        Toast.makeText(requireContext(), "Long click detected", Toast.LENGTH_SHORT).show()

    }


}