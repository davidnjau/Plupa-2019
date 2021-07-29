package com.ben.planninact.room_persitence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ben.planninact.room_persitence.entity.*

@Database(

    entities = [
        PartDetailsTitle::class, PartDetailsContent::class,
        FirstSchedule::class, SecondSchedule::class, ThirdSchedule::class
    ],
    version = 1,
    exportSchema = false

)

abstract class PlupaRoomDatabase : RoomDatabase(){

    abstract fun planning_dao() : PlanningDao
    companion object{
        @Volatile
        private var INSTANCE : PlupaRoomDatabase? = null
        fun getDatabase(context: Context) : PlupaRoomDatabase{

            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlupaRoomDatabase::class.java,
                    "plupa_db"
                )
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }

}