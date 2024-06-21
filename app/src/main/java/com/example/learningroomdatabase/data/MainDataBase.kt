package com.example.learningroomdatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NameEntity::class
    ],
    version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDataBase(context: Context) : MainDataBase{
            return Room.databaseBuilder(
             context,
                MainDataBase::class.java,
                "test.db"
            ).build()
        }
    }
}