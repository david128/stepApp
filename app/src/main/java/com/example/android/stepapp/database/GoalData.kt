package com.example.android.stepapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "goal_table")
data class GoalData(
    @PrimaryKey(autoGenerate = true)
    var goalID: Long =0L,
    @ColumnInfo(name = "goal_name")
    var goalName : String = "",
    @ColumnInfo(name = "goal_target")
    var stepGoal : Int = 0
) : Parcelable
