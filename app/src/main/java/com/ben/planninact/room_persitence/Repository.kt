package com.ben.planninact.room_persitence

import android.os.Build
import android.text.Html
import androidx.lifecycle.LiveData
import com.ben.planninact.helper_class.PlupaPojo
import com.ben.planninact.room_persitence.entity.*

class Repository(private val planningDao: PlanningDao) {

    val urlsTitleList: LiveData<List<PartDetailsTitle>> = planningDao.getLiveUrlsTitle()
    val firstScheduleList: LiveData<List<FirstSchedule>> = planningDao.getLiveUrlsFirstSchedule()
    val secondScheduleList: LiveData<List<SecondSchedule>> = planningDao.getLiveUrlsSecondSchedule()
    val thirdScheduleList: LiveData<List<ThirdSchedule>> = planningDao.getLiveUrlsThirdSchedule()


    suspend fun insertPartData(partDetailsTitle: PartDetailsTitle){

        val partNumber = partDetailsTitle.part_number
        val isPartNumber = planningDao.checkPartTitle(partNumber)
        if (!isPartNumber){
            planningDao.insertPartTitle(partDetailsTitle)
        }
    }

    suspend fun insertPartContents(partDetailsContent: PartDetailsContent){

        val part_heading = partDetailsContent.part_heading
        val part_description = partDetailsContent.part_description
        val isPartNumber = planningDao.checkPartDescription(part_description, part_heading)
        if (!isPartNumber){
            planningDao.insertPartDetails(partDetailsContent)
        }
    }

    suspend fun insertFirstSchedule(firstSchedule: FirstSchedule){

        val partNumber = firstSchedule.part_name
        val isPartNumber = planningDao.checkFirstSchedule(partNumber)
        if (!isPartNumber){
            planningDao.insertFirstSchedule(firstSchedule)
        }
    }

    suspend fun insertSecondSchedule(secondSchedule: SecondSchedule){

        val partNumber = secondSchedule.part_name
        val isPartNumber = planningDao.checkSecondSchedule(partNumber)
        if (!isPartNumber){
            planningDao.insertSecondSchedule(secondSchedule)
        }
    }
    suspend fun insertThirdSchedule(thirdSchedule: ThirdSchedule){

        val partNumber = thirdSchedule.title
        val isPartNumber = planningDao.checkThirdSchedule(partNumber)
        if (!isPartNumber){
            planningDao.insertThirdSchedule(thirdSchedule)
        }
    }

    suspend fun getPlupaTitle(): List<PartDetailsTitle> {
        return planningDao.getPlupaTitles()
    }
    suspend fun getPlupaContent():List<PartDetailsContent>{
        return planningDao.getPlupaContent()
    }
    suspend fun getPlupaContentByPartTitle(title: String):List<PartDetailsContent>{
        return planningDao.getPlupaContentByPartTitle(title)
    }
    suspend fun getContentByPartId(id: Int):PartDetailsContent{
        return planningDao.getContentByPartId(id)
    }
    suspend fun getFirstScheduleByPartId(id: Int):FirstSchedule{
        return planningDao.getFirstScheduleByPartId(id)
    }
    suspend fun getSecondScheduleByPartId(id: Int):SecondSchedule{
        return planningDao.getSecondScheduleByPartId(id)
    }
    suspend fun getThirdScheduleByPartId(id: Int):ThirdSchedule{
        return planningDao.getThirdScheduleByPartId(id)
    }
    suspend fun getFirstSchedule():List<FirstSchedule>{
        return planningDao.getPlupaFirstSchedule()
    }
    suspend fun getSecondSchedule():List<SecondSchedule>{
        return planningDao.getPlupaSecondSchedule()
    }
    suspend fun getThirdSchedule():List<ThirdSchedule>{
        return planningDao.getPlupaThirdSchedule()
    }

    suspend fun getRevisedPlupaDetails(partQuery: String):List<PlupaPojo>{

        val displayDataList = ArrayList<PlupaPojo>()
        val plupaDetailsList = getPlupaResults(partQuery)
        for (items in plupaDetailsList){

            val dbId = items.dbId
            val dbHeading = items.dbHeading
            val dbBody = items.dbBody
            val dbType = items.dbType

            var displayData = ""

            if (dbType == "third_schedule"){

                displayData += dbBody + "\n"
                displayData += dbHeading

            }
            displayData = dbBody

            val plupaPojo = PlupaPojo(
                dbId, dbHeading, dbBody, dbType
            )


            displayDataList.add(plupaPojo)
        }

        return displayDataList

    }

    suspend fun getPlupaResults(partQuery: String):List<PlupaPojo>{

        val plupaHeadDetailsList = planningDao.getPlupaTitles()
        val plupaDetailsList = planningDao.getPlupaResults()
        val firstScheduleList = planningDao.getPlupaFirstSchedule()
        val secondScheduleList = planningDao.getPlupaSecondSchedule()
        val thirdScheduleList = planningDao.getPlupaThirdSchedule()

        val partHeadingList1 = ArrayList<PlupaPojo>()

        for (titles in plupaHeadDetailsList){

            val id = titles.id.toString()
            val partName = titles.part_name
            val partNumber = titles.part_number

            if (partName.toLowerCase().contains(partQuery.toLowerCase())){

                val plupaPojo = PlupaPojo(id, partName, partNumber, "plupa_content")
                partHeadingList1.add(plupaPojo)
            }



        }
        for (content in plupaDetailsList){

            val contentId = content.id.toString()
            val contentHeading = content.part_heading
            val contentDesc = content.part_description

            if (contentHeading.toLowerCase().contains(partQuery.toLowerCase()) ||
                contentDesc.toLowerCase().contains(partQuery.toLowerCase())){

                val plupaPojo = PlupaPojo(contentId, contentHeading, contentDesc,
                    "plupa_content")
                partHeadingList1.add(plupaPojo)
            }



        }
        for (content in firstScheduleList){

            val contentId = content.id.toString()
            val contentHeading = content.part_name
            val contentDesc = content.content_description

            if (contentDesc.toLowerCase().contains(partQuery.toLowerCase())){

                val plupaPojo = PlupaPojo(contentId, contentHeading, contentDesc,
                    "first_schedule")
                partHeadingList1.add(plupaPojo)
            }


        }
        for (content in secondScheduleList){

            val contentId = content.id.toString()
            val contentHeading = content.part_name
            val contentDesc = content.content_description
            if (contentDesc.toLowerCase().contains(partQuery.toLowerCase())){

                val plupaPojo = PlupaPojo(contentId, contentHeading, contentDesc,
                    "second_schedule")
                partHeadingList1.add(plupaPojo)
            }


        }
        for (content in thirdScheduleList){

            val contentId = content.id.toString()
            val contentHeading = content.title
            val contentDesc = content.content_description
            if (contentDesc.toLowerCase().contains(partQuery.toLowerCase()) ||
                contentHeading.toLowerCase().contains(partQuery.toLowerCase()) ){

                val plupaPojo = PlupaPojo(contentId, contentHeading, contentDesc,
                    "third_schedule")
                partHeadingList1.add(plupaPojo)
            }


        }


        return partHeadingList1
    }

    suspend fun getPlupaDetailsById(id : String, type: String) : PlupaPojo {

        var dbId  = ""
        var dbHeading  = ""
        var dbContent  = ""

        if (type == "plupa_content"){
            val plupaContent = planningDao.getPlupaDetailsById(id)
            dbId = plupaContent.id.toString()
            dbHeading = plupaContent.part_heading
            dbContent = plupaContent.part_description

        }else if (type == "first_schedule"){
            val firstScheduleData = planningDao.getFirstScheduleByPartId(id.toInt())
            dbId = firstScheduleData.id.toString()
            dbHeading = firstScheduleData.part_name
            dbContent = firstScheduleData.content_description

        }else if(type == "second_schedule"){
            val secondScheduleData = planningDao.getSecondScheduleByPartId(id.toInt())
            dbId = secondScheduleData.id.toString()
            dbHeading = secondScheduleData.part_name
            dbContent = secondScheduleData.content_description

        }else if (type == "third_schedule"){
            val thirdScheduleData = planningDao.getThirdScheduleByPartId(id.toInt())
            var dbBody = ""
            dbId = thirdScheduleData.id.toString()
            dbHeading = thirdScheduleData.title
            dbBody = thirdScheduleData.content_description
            dbBody = "$dbHeading \n $dbBody"
            dbContent = dbBody

        }

        val plupaPojo = PlupaPojo(dbId, dbHeading, dbContent, type)


        return plupaPojo

    }

}