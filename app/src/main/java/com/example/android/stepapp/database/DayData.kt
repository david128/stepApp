package com.example.android.stepapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.stepapp.DateFormatting
import java.util.*




@Entity(tableName = "day_table")
data class DayData(
    @PrimaryKey(autoGenerate = true)
    var dayID: Long =0L,
    @ColumnInfo(name = "step_date")
    val stepDate : String = "",
    @ColumnInfo(name = "step_count")
    val stepCount : Int = 0,
    @ColumnInfo(name = "step_goal")
    val stepGoal : Int = 0
)
