package com.example.android.stepapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GoalData::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class GoalDatabase : RoomDatabase(){

    abstract val goalDatabaseDao:GoalDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: GoalDatabase?=null

        fun getInstance(context: Context):GoalDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance==null){
                    instance= Room.databaseBuilder(context.applicationContext,
                        GoalDatabase::class.java,"goal_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}