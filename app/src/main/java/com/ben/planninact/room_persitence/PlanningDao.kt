package com.ben.planninact.room_persitence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ben.planninact.room_persitence.entity.*

@Dao
interface PlanningDao {

    /**
     * Check if exists
     */

    @Query("SELECT * FROM part_details_title WHERE part_number =:part_number")
    fun checkPartTitle(part_number: String): Boolean

    @Query("SELECT * FROM plupa_data_info WHERE part_description =:part_description AND part_heading =:part_heading")
    fun checkPartDescription(part_description: String, part_heading: String): Boolean

    @Query("SELECT * FROM first_schedule WHERE part_name =:part_name")
    fun checkFirstSchedule(part_name: String): Boolean

    @Query("SELECT * FROM second_schedule WHERE part_name =:part_name")
    fun checkSecondSchedule(part_name: String): Boolean

    @Query("SELECT * FROM third_schedule WHERE title =:title")
    fun checkThirdSchedule(title: String): Boolean

    /**
     * Insert Data
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartTitle(partDetailsTitle: PartDetailsTitle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartDetails(partDetailsContent: PartDetailsContent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstSchedule(firstSchedule: FirstSchedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecondSchedule(secondSchedule: SecondSchedule)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThirdSchedule(thirdSchedule: ThirdSchedule)

    /**
     * Get Details
     */
    @Query("SELECT * from part_details_title ORDER BY part_number ASC")
    suspend fun getPlupaTitles(): List<PartDetailsTitle>

    @Query("SELECT * from plupa_data_info ORDER BY part_heading ASC")
    suspend fun getPlupaContent(): List<PartDetailsContent>

    @Query("SELECT * from plupa_data_info WHERE part_id =:title")
    suspend fun getPlupaContentByPartTitle(title: String): List<PartDetailsContent>

    @Query("SELECT * from plupa_data_info WHERE id =:id")
    suspend fun getContentByPartId(id: Int): PartDetailsContent

    @Query("SELECT * from first_schedule WHERE id =:id")
    suspend fun getFirstScheduleByPartId(id: Int): FirstSchedule

    @Query("SELECT * from second_schedule WHERE id =:id")
    suspend fun getSecondScheduleByPartId(id: Int): SecondSchedule

    @Query("SELECT * from third_schedule WHERE id =:id")
    suspend fun getThirdScheduleByPartId(id: Int): ThirdSchedule

    @Query("SELECT * from first_schedule ORDER BY part_name ASC")
    suspend fun getPlupaFirstSchedule(): List<FirstSchedule>

    @Query("SELECT * from second_schedule ORDER BY part_name ASC")
    suspend fun getPlupaSecondSchedule(): List<SecondSchedule>

    @Query("SELECT * from third_schedule ORDER BY title ASC")
    suspend fun getPlupaThirdSchedule(): List<ThirdSchedule>

    /**
     * Get Live Details
     */
    @Query("SELECT * from part_details_title")
    fun getLiveUrlsTitle(): LiveData<List<PartDetailsTitle>>

    @Query("SELECT * from plupa_data_info")
    fun getLiveUrlsContent(): LiveData<List<PartDetailsContent>>

    @Query("SELECT * from first_schedule")
    fun getLiveUrlsFirstSchedule(): LiveData<List<FirstSchedule>>

    @Query("SELECT * from second_schedule")
    fun getLiveUrlsSecondSchedule(): LiveData<List<SecondSchedule>>

    @Query("SELECT * from third_schedule")
    fun getLiveUrlsThirdSchedule(): LiveData<List<ThirdSchedule>>

    @Query("SELECT * from plupa_data_info")
    suspend fun getPlupaResults(): List<PartDetailsContent>

    @Query("SELECT * from plupa_data_info WHERE id =:id")
    suspend fun getPlupaDetailsById(id: String): PartDetailsContent
}