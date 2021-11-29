package com.ben.planninact.adapter

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.MainActivity
import com.ben.planninact.R
import com.ben.planninact.room_persitence.ViewModel
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


class TitleScheduleAdapter(
    private val context: Context,
    private var plupaInfoList: List<String>,
    private var idList: ArrayList<String>,


    ) :
    RecyclerView.Adapter<TitleScheduleAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvPlupaHeading : TextView = itemView.findViewById(R.id.tvPartHeading)

        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            val viewModel = ViewModel(context.applicationContext as Application)
            val partNumber = plupaInfoList[position]

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val part_title = sharedpreferences.getString("part_title", null)
            var partTitle = ""

            val id = position + 1

            when (part_title) {
                "plupa_title" -> {
                    partTitle = "plupa_content"
                }
                "first_schedule" -> {
                    partTitle = "first_schedule"
                }
                "second_schedule" -> {
                    partTitle = "second_schedule"
                }
                "third_schedule" -> {
                    partTitle = "third_schedule"
                }

            }

            val editor: SharedPreferences.Editor = sharedpreferences.edit()

            editor.putString("part_type", partTitle)
            editor.putString("plupa_id", id.toString())

            editor.putString("search_results", null)
            editor.apply()

//            editor.putBoolean("page_detail", true)
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
                R.layout.title_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        var viewModel = ViewModel(context.applicationContext as Application)

        val part_name = plupaInfoList[position]

        val formattedHtmlContent = HtmlFormatter.formatHtml(
            HtmlFormatterBuilder().setHtml(part_name)
                .setImageGetter(HtmlResImageGetter(holder.tvPlupaHeading.context))
        )

        holder.tvPlupaHeading.text = formattedHtmlContent


    }

    override fun getItemCount(): Int {
        return plupaInfoList.size
    }



}