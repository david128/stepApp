package com.example.android.stepapp.goal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stepapp.database.GoalData
import com.example.android.stepapp.R


class ListAdapter(private val listener: OnGoalListner, private val activeName: String) :RecyclerView.Adapter<ListAdapter.ViewHolder>(){

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
                listener.onGoalClick(goalList[adapterPosition])
            }

        }

        override fun onLongClick(v: View?): Boolean {

            if (adapterPosition != RecyclerView.NO_POSITION){
                val id = goalList[adapterPosition].goalID
                listener.onGoalLongClick(goalList[adapterPosition].goalName)
            }
            return true
        }


    }

    class ActiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val goalName: TextView
        val stepGoal: TextView
        init {
            // Define click listener for the ViewHolder's View.
            goalName = itemView.findViewById(R.id.active_goal_row_goal_name)
            stepGoal = itemView.findViewById(R.id.active_goal_row_step_goal)
        }
    }



    interface OnGoalListner{
        fun onGoalClick(goal:GoalData)
        fun onGoalLongClick( name: String)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        Log.d("CVH", parent.toString() + " + " + viewType.toString() )
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.goal_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = goalList[position]
        holder.stepGoal.text = currentItem.stepGoal.toString()
        holder.goalName.text = currentItem.goalName



    }

    fun getItem(position: Int) : GoalData{
        Log.d("CVH", "position " + " + " + position.toString() )
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