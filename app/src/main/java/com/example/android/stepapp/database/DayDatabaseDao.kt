package com.example.android.stepapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface DayDatabaseDao {


    @Insert
    fun insert(day: DayData)

    @Update
    fun update(day: DayData)

    @Query(value = "SELECT * FROM day_table WHERE dayID = :key")
    fun get(key:Long) : DayData

    @Query(value = "DELETE FROM day_table")
    suspend fun clear()

    @Query(value="SELECT * FROM day_table ORDER BY dayID DESC")
    fun getAllDays() : LiveData<List<DayData>>

    @Query(value="SELECT * FROM day_table ORDER BY dayID DESC LIMIT 1")
    fun getToday() : DayData?

    @Query("SELECT EXISTS(SELECT * FROM day_table WHERE step_date = :queryDate)")
    suspend fun doesDayExist(queryDate:String) : Boolean

    @Query(value="SELECT * FROM day_table  WHERE step_date = :queryDate")
    fun getSpecificDay(queryDate:String ) : DayData?
}