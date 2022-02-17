package com.example.android.stepapp.goal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.R

import androidx.fragment.app.FragmentTransaction
import com.example.android.stepapp.addGoal.AddGoalFragment
import com.example.android.stepapp.database.DayData


class ListAdapter(private val listener: OnGoalListner) :RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    private var goalList = emptyList<GoalData>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener,View.OnLongClickListener {
        val goalName: TextView
        val stepGoal: TextView

        init {
            // Define click listener for the ViewHolder's View.
            goalName = itemView.findViewById(R.id.goal_row_goal_name)
            stepGoal = itemView.findViewById(R.id.goal_row_step_goal)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {

            if (adapterPosition != RecyclerView.NO_POSITION){
                val id = goalList[adapterPosition].goalID
                listener.onGoalClick(id,goalList[adapterPosition].GoalName,goalList[adapterPosition].stepGoal )
            }

        }

        override fun onLongClick(v: View?): Boolean {

            if (adapterPosition != RecyclerView.NO_POSITION){
                val id = goalList[adapterPosition].goalID
                listener.onGoalLongClick(goalList[adapterPosition].GoalName)
            }
            return true
        }


    }



    interface OnGoalListner{
        fun onGoalClick(id : Long, name: String, steps:Int)
        fun onGoalLongClick( name: String)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.goal_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = goalList[position]
        holder.stepGoal.text = currentItem.stepGoal.toString()
        holder.goalName.text = currentItem.GoalName



    }

    fun getItem(position: Int) : GoalData{

        return goalList[position]
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    fun setData(goal: List<GoalData>){
        this.goalList =goal
        notifyDataSetChanged()
    }
}