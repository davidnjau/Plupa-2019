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
import com.ben.planninact.room_persitence.entity.ThirdSchedule
import kotlinx.android.synthetic.main.fragment_home.*
import net.cachapa.expandablelayout.ExpandableLayout
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


class ThirdScheduleAdapter(
    private val context: Context,
    private var plupaInfoList: List<ThirdSchedule>,


    ) :
    RecyclerView.Adapter<ThirdScheduleAdapter.Pager2ViewHolder>() {

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
            val partNumber = plupaInfoList[position].title
            val id = plupaInfoList[position].id

            val sharedpreferences = context.getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val editor: SharedPreferences.Editor = sharedpreferences.edit()

            editor.putString("part_type", "third_schedule")
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

        val part_name = plupaInfoList[position].title
        val part_number = plupaInfoList[position].content_description

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