package com.ben.planninact.room_persitence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plupa_data_info")
data class PartDetailsContent(
    val part_heading: String,
    val part_description: String,
    val part_id: String,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
