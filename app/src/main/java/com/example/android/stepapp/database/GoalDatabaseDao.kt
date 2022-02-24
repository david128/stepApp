package com.example.android.stepapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GoalDatabaseDao {


    @Insert
    fun insert(goal: GoalData)

    @Update
    fun update(goal: GoalData)

    @Delete
    suspend fun deleteGoal(goal:GoalData)

    @Query("SELECT * FROM goal_table WHERE goalID = :key")
    fun get(key:Long) : GoalData

    @Query("DELETE FROM goal_table")
    fun clear()

    @Query("SELECT * FROM goal_table ORDER BY goalID DESC")
    fun getAllGoals() : LiveData<List<GoalData>>

    @Query("SELECT * FROM goal_table WHERE goal_name = :name")
    suspend fun getGoalByName(name:String) : GoalData?




}