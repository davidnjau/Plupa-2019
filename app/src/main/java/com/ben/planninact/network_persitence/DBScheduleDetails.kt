package com.ben.planninact.network_persitence

data class DBScheduleDetails(

    val partDetails: List<DbPartTitle>,
    val firstSchedule: List<DbFirstSchedule>,
    val secondSchedule: List<DbSecondSchedule>,
    val thirdSchedule: List<DbThirdSchedule>

)
data class DbPartTitle(
    val id: String,
    val part_number: String,
    val part_name: String,
    val content_description: List<DbPartDetails>
)
data class DbPartDetails(
    val id: String,
    val part_description:String,
    val part_heading: String,
    val part_id:String
)
data class DbFirstSchedule(
    val id:String,
    val part_name: String,
    val content_description: String
)
data class DbSecondSchedule(
    val id:String,
    val part_name: String,
    val content_description: String
)
data class DbThirdSchedule(
    val id:String,
    val title: String,
    val content_description: String
)
