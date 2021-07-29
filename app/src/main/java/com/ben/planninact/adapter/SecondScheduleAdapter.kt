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
import com.ben.planninact.room_persitence.entity.SecondSchedule
import net.cachapa.expandablelayout.ExpandableLayout


class SecondScheduleAdapter(
    private val context: Context,
    private var plupaInfoList: List<SecondSchedule>,

    ) :
    RecyclerView.Adapter<SecondScheduleAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvPlupaHeading : TextView = itemView.findViewById(R.id.tvPartHeading)

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

            editor.putString("part_type", "second_schedule")
            editor.putString("plupa_id", id.toString())
            editor.apply();

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
        val id = plupaInfoList[position].id

        holder.tvPlupaHeading.text = "PART $part_name"



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