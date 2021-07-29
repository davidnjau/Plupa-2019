package com.ben.planninact.room_persitence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "first_schedule")
data class FirstSchedule(

    val part_name: String,
    val content_description: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}