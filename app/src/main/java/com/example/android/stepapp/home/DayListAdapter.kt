package com.example.android.stepapp.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stepapp.R

import com.example.android.stepapp.database.DayData


class DayListAdapter :RecyclerView.Adapter<DayListAdapter.ViewHolder>(){

    private var dayList = emptyList<DayData>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayDate: TextView
        val dayStepGoal: TextView
        val dayStepCount: TextView

        init {
            // Define click listener for the ViewHolder's View.
            dayDate = itemView.findViewById(R.id.day_row_date)
            dayStepCount = itemView.findViewById(R.id.day_row_step_count)
            dayStepGoal = itemView.findViewById(R.id.day_row_step_goal)
        }





    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.day_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dayList[position]
        holder.dayDate.text = currentItem.stepDate
        holder.dayStepCount.text = currentItem.stepCount.toString() + "/"
        holder.dayStepGoal.text = currentItem.stepGoal.toString()

    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    fun setData(day: List<DayData>){
        this.dayList =day
        notifyDataSetChanged()
    }
}