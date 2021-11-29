package com.ben.planninact.adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.MainActivity
import com.ben.planninact.R
import com.ben.planninact.room_persitence.ViewModel
import com.ben.planninact.room_persitence.entity.FirstSchedule
import com.ben.planninact.room_persitence.entity.PartDetailsTitle
import net.cachapa.expandablelayout.ExpandableLayout


class FirstScheduleAdapter(
    private val context: Context,
    private var plupaInfoList: List<FirstSchedule>,


    ) :
    RecyclerView.Adapter<FirstScheduleAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


//        val expanded_menu : ExpandableLayout = itemView.findViewById(R.id.expanded_menu)
        val tvPlupaHeading : TextView = itemView.findViewById(R.id.tvPartHeading)
//        val recyclerViewNav : RecyclerView = itemView.findViewById(R.id.recyclerViewNav)


        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            val viewModel = ViewModel(context.applicationContext as Application)
            val partNumber = plupaInfoList[position].part_name
            val id = plupaInfoList[position].id

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val editor: SharedPreferences.Editor = sharedpreferences.edit()

            editor.putString("part_type", "first_schedule")
            editor.putString("plupa_id", id.toString())
            editor.apply();

            editor.putString("search_results", null)
            editor.apply()

            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.part_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        var viewModel = ViewModel(context.applicationContext as Application)

        val part_name = plupaInfoList[position].part_name
        val part_number = plupaInfoList[position].content_description
//        holder.tvPlupaHeading.text = stripHtml("($part_number.) $part_name")
        holder.tvPlupaHeading.text = "PART $part_name"

//        val plupaDataInfoList = plupaData.plupaDataInfoList

//        val plupaAdapter = PlupaSearchAdapter(
//            context, plupaDataInfoList
//        )
//        holder.recyclerViewNav.layoutManager = LinearLayoutManager(context)
//        holder.recyclerViewNav.setHasFixedSize(true)
//        holder.recyclerViewNav.adapter = plupaAdapter


//        val part_number = plupaDataList[position].part_number



//        holder.tvPartHeading.text = stripHtml(part_heading)


    }

    override fun getItemCount(): Int {
        return plupaInfoList.size
    }

    fun stripHtml(html1: String): String {

        val html = html1.replace("<br />", "")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun getRoman(num:Int):String{

        var roman = ""

        roman = when (num){
            0->"I"
            1->"II"
            2->"III"
            3->"VI"
            4->"V"
            5->"VI"
            6->"VII"
            7->"VIII"
            else -> ""
        }

        return roman
    }


}