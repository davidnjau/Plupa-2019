package com.ben.planninact.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.MainActivity
import com.ben.planninact.R
import com.ben.planninact.room_persitence.entity.PartDetailsContent
import java.util.*

class PlupaAdapterDetails(
    private val context: Context,
    private var plupaDataList: List<PartDetailsContent>,


    ) :
    RecyclerView.Adapter<PlupaAdapterDetails.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvPartHeading : TextView = itemView.findViewById(R.id.tvPartHeading)


        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            val id = plupaDataList[position].id.toString()

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val editor: SharedPreferences.Editor = sharedpreferences.edit()

            editor.putString("part_type", "plupa_content")
            editor.putString("plupa_id", id)
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

        val part_heading = plupaDataList[position].part_heading
        holder.tvPartHeading.text = stripHtml(part_heading)


    }

    override fun getItemCount(): Int {
        return plupaDataList.size
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


}