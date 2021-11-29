package com.ben.planninact.room_persitence

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ben.planninact.network_persitence.DbFirstSchedule
import com.ben.planninact.network_persitence.DbPartTitle
import com.ben.planninact.network_persitence.DbSecondSchedule
import com.ben.planninact.network_persitence.DbThirdSchedule
import com.ben.planninact.room_persitence.entity.*
import kotlinx.coroutines.*

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository

    val titleList: LiveData<List<PartDetailsTitle>>
    val firstScheduleList: LiveData<List<FirstSchedule>>
    val secondScheduleList: LiveData<List<SecondSchedule>>
    val thirdScheduleList: LiveData<List<ThirdSchedule>>

    init {

        val dynamicDao = PlupaRoomDatabase.getDatabase(application).planning_dao()
        repository = Repository(dynamicDao)
        titleList = repository.urlsTitleList
        firstScheduleList = repository.firstScheduleList
        secondScheduleList = repository.secondScheduleList
        thirdScheduleList = repository.thirdScheduleList

    }

    fun getTitles():List<PartDetailsTitle> = runBlocking {
        repository.getPlupaTitle()
    }
    fun getContent():List<PartDetailsContent> = runBlocking {
        repository.getPlupaContent()
    }
    fun getContentByPartTitle(partId: String) = runBlocking {
        repository.getPlupaContentByPartTitle(partId)
    }
    fun getContentByPartId(id: Int) = runBlocking {
        repository.getContentByPartId(id)
    }
    fun getFirstScheduleByPartId(id: Int) = runBlocking {
        repository.getFirstScheduleByPartId(id)
    }
    fun getSecondScheduleByPartId(id: Int) = runBlocking {
        repository.getSecondScheduleByPartId(id)
    }
    fun getThirdScheduleByPartId(id: Int) = runBlocking {
        repository.getThirdScheduleByPartId(id)
    }
    fun getFirstSchedule() = runBlocking {
        repository.getFirstSchedule()
    }
    fun getSecondSchedule() = runBlocking {

        repository.getSecondSchedule()
    }


    fun getThirdSchedule() = runBlocking {
        repository.getThirdSchedule()
    }

    fun insertPartData(partDetailsList : List<DbPartTitle>){

        CoroutineScope(Dispatchers.IO).launch {

            for (items in partDetailsList){

                val part_number = items.part_number
                val part_name = items.part_name

                val partDetailsTitle = PartDetailsTitle(part_name, part_number)
                repository.insertPartData(partDetailsTitle)

                val contentList = items.content_description
                for (contents in contentList){

                    val part_heading = contents.part_heading
                    val part_description = contents.part_description
                    val part_id = contents.part_id

                    val contentsData = PartDetailsContent(part_heading, part_description, part_id)
                    repository.insertPartContents(contentsData)

                }

            }


        }

    }

    fun insertFirstSchedule(firstScheduleList: List<DbFirstSchedule>){

        CoroutineScope(Dispatchers.IO).launch {

            for (items in firstScheduleList){

                val part_name = items.part_name
                val content_description = items.content_description

                val firstSchedule = FirstSchedule(part_name,content_description)
                repository.insertFirstSchedule(firstSchedule)

            }


        }

    }
    fun insertSecondSchedule(secondScheduleList: List<DbSecondSchedule>){

        CoroutineScope(Dispatchers.IO).launch {
            for (items in secondScheduleList){

                val part_name = items.part_name
                val content_description = items.content_description

                val secondSchedule = SecondSchedule(part_name,content_description)
                repository.insertSecondSchedule(secondSchedule)

            }
        }

    }
    fun insertThirdSchedule(thirdScheduleList: List<DbThirdSchedule>){



        CoroutineScope(Dispatchers.IO).launch {
            for (items in thirdScheduleList){

                val part_name = items.title
                val content_description = items.content_description

                val thirdSchedule = ThirdSchedule(part_name,content_description)
                repository.insertThirdSchedule(thirdSchedule)

            }
        }

    }

    fun getPlupaResults(partQuery: String) = runBlocking{
        repository.getRevisedPlupaDetails(partQuery)
    }
    fun getPlupaDetailsById(id:String, type: String) = runBlocking{
        repository.getPlupaDetailsById(id, type)
    }

}