package com.example.android.stepapp.updateGoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.stepapp.R
import com.example.android.stepapp.database.GoalData


class UpdateGoalFragment : Fragment() {

    fun newInstance(id:Int, goalName: String, stepGoal: Int): UpdateGoalFragment? {
        val fragment = UpdateGoalFragment()
        val args = Bundle()
        args.putInt("id", id)
        args.putString("goalName", goalName)
        args.putInt("stepGoal", stepGoal)

        fragment.setArguments(args)
        return fragment
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val id = arguments!!.getInt("id", 0)
        val goalName = arguments!!.getString("goalName", "")
        val stepGoal = arguments!!.getString("stepGoal", "")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_goal, container, false)
    }


}