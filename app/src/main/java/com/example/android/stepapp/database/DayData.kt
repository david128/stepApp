package com.example.android.stepapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*




@Entity(tableName = "day_table")
data class DayData(
    @PrimaryKey(autoGenerate = true)
    var dayID: Long =0L,
    @ColumnInfo(name = "step_date")
    var stepDate : String = "" ,
    @ColumnInfo(name = "step_count")
    var stepCount : Int = 0,
    @ColumnInfo(name = "step_goal")
    var stepGoal : Int = 0
)
