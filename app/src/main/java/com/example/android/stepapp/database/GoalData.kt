package com.example.android.stepapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal_table")
data class GoalData(
    @PrimaryKey(autoGenerate = true)
    var goalID: Long =0L,
    @ColumnInfo(name = "goal_name")
    val goalName : String = "",
    @ColumnInfo(name = "step_goal")
    val stepGoal : Int = 0
)
