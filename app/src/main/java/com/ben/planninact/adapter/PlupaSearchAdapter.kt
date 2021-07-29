package com.ben.planninact.adapter

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


        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            val id = plupaDataList[position].id

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putString("part_type", "plupa_content")
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
                R.layout.part_heading_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {

        val part_heading = plupaDataList[position].part_heading
        holder.tvPartHeading.text = Formatter().stripHtml(part_heading)


    }

    override fun getItemCount(): Int {
        return plupaDataList.size
    }



}