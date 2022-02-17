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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stepapp.R
import com.example.android.stepapp.addGoal.AddGoalFragment
import com.example.android.stepapp.database.GoalDatabase
import com.example.android.stepapp.databinding.FragmentGoalsBinding
import com.example.android.stepapp.databinding.FragmentHomeBinding
import com.example.android.stepapp.updateGoal.UpdateGoalFragment


/**
 * A simple [Fragment] subclass.
 * Use the [GoalsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoalsFragment : Fragment(), ListAdapter.OnGoalListner {


    private lateinit var viewModel: GoalsViewModel
    private lateinit var addGoalFragment : AddGoalFragment
    private lateinit var updateGoalFragment: UpdateGoalFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val binding : FragmentGoalsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_goals, container, false)
        binding.floatingActionButton.setOnClickListener{

            addGoalFragment= AddGoalFragment()
            val transaction = getParentFragmentManager().beginTransaction()
            transaction.replace(R.id.nav_host_fragment,addGoalFragment)
            transaction.commit()

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

    override fun onGoalClick(id : Long, name: String, steps:Int) {

        updateGoalFragment = UpdateGoalFragment().newInstance(id,name,steps)!!
        val transaction = getParentFragmentManager().beginTransaction()
        transaction.replace(R.id.nav_host_fragment,updateGoalFragment)
        transaction.commit()
    }

    override fun onGoalLongClick(name: String) {
        Toast.makeText(requireContext(), "Long click detected", Toast.LENGTH_SHORT).show()

    }


}