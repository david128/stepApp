package com.example.android.stepapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GoalDatabaseDao {


    @Insert
    fun insert(goal: GoalData)

    @Update
    fun update(goal: GoalData)

    @Query(value = "SELECT * FROM goal_table WHERE goalID = :key")
    fun get(key:Long) : GoalData

    @Query(value = "DELETE FROM goal_table")
    fun clear()

    @Query(value="SELECT * FROM goal_table ORDER BY goalID DESC")
    fun getAllGoals() : LiveData<List<GoalData>>




}