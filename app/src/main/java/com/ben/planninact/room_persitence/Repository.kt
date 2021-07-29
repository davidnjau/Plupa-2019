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
    suspend fun getPlupaResults(partQuery: String):List<String>{

        val partHeadingList = ArrayList<PlupaPojo>()
        val plupaDataInfoList = planningDao.getPlupaResults()

        val partHeadingList1 = ArrayList<String>()


        for (item in plupaDataInfoList){

            val id = item.id
            val partHeading = item.part_heading
            val partDescr = item.part_description
            val partId = item.part_id

            if (partHeading.toLowerCase().contains(partQuery.toLowerCase())){

                val plupaDataInfo = PlupaPojo(id.toString(), stripHtml(partHeading), partDescr, partId)

                partHeadingList1.add(id.toString())
            }

        }

        return partHeadingList1
    }
    fun stripHtml(html1: String): String {

        val html = html1.replace("<br />", "")
        val html2 = html.replace("</p>", "")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html2, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html2).toString()
        }
    }
    suspend fun getPlupaDetailsById(id : String) : PartDetailsContent {

        return planningDao.getPlupaDetailsById(id)

    }

}