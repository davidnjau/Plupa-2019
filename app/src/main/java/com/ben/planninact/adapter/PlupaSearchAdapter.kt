package com.ben.planninact.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.MainActivity
import com.ben.planninact.R
import com.ben.planninact.helper_class.Formatter
import com.ben.planninact.helper_class.PlupaPojo


class PlupaSearchAdapter(
    private val context: Context,
    private var plupaDataList: List<PlupaPojo>,


    ) :
    RecyclerView.Adapter<PlupaSearchAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvPartHeading : TextView = itemView.findViewById(R.id.tvPartHeading)
        val tvPartDescription : TextView = itemView.findViewById(R.id.tvPartDescription)


        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            val id = plupaDataList[position].dbId
            val type = plupaDataList[position].dbType

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val editor: SharedPreferences.Editor = sharedpreferences.edit()

            editor.putString("part_type", type)
            editor.putString("plupaId", id)
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
                R.layout.part_result_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        val part_heading = plupaDataList[position].dbBody.toString()

        val part_description = plupaDataList[position].dbHeading
        val type = plupaDataList[position].dbType

        var dbTypeData = ""
        when (type) {
            "plupa_content" -> {dbTypeData = "Plupa Content" }
            "first_schedule" -> {dbTypeData = "First Schedule" }
            "second_schedule" -> {dbTypeData = "Second Schedule" }
            "third_schedule" -> {dbTypeData = "Third Schedule" }
        }

        val description = "($dbTypeData - $part_description)"

        holder.tvPartHeading.text = Formatter().stripHtml(part_heading)
        holder.tvPartDescription.text = Formatter().stripHtml(description)



    }

    override fun getItemCount(): Int {
        return plupaDataList.size
    }



}