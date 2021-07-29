package com.ben.planninact.room_persitence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "part_details_title")
data class PartDetailsTitle(
    val part_name: String,
    val part_number: String
    ){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
