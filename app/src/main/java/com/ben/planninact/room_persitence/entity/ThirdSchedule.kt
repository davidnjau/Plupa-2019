package com.ben.planninact.room_persitence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "third_schedule")
data class ThirdSchedule(

    val title: String,
    val content_description: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}