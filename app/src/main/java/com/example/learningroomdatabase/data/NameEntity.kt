package com.example.learningroomdatabase.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tableEntity") // based on this class, room understands that a table is being created here

data class NameEntity(
    @PrimaryKey(autoGenerate = true)
    /* the room library will give us a unique identifier
    in the form of numbers like: 0,1,2.. */
    val id : Int? = null,
    val name: String
)
