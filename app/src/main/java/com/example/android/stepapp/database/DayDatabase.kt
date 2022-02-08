package com.example.android.stepapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DayData::class],version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class DayDatabase : RoomDatabase(){

    abstract val dayDatabaseDao:DayDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: DayDatabase?=null

        fun getInstance(context: Context):DayDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance==null){
                    instance= Room.databaseBuilder(context.applicationContext,
                        DayDatabase::class.java,"day_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}