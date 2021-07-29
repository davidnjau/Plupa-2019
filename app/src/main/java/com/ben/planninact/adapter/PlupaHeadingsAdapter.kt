package com.ben.planninact.adapter

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.R
import com.ben.planninact.room_persitence.ViewModel
import com.ben.planninact.room_persitence.entity.PartDetailsTitle
import net.cachapa.expandablelayout.ExpandableLayout
import java.util.*


class PlupaHeadingsAdapter(
    private val context: Context,
    private var plupaInfoList: List<PartDetailsTitle>,


    ) :
    RecyclerView.Adapter<PlupaHeadingsAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        val expanded_menu : ExpandableLayout = itemView.findViewById(R.id.expanded_menu)
        val tvPlupaHeading : TextView = itemView.findViewById(R.id.tvPlupaHeading)
        val recyclerViewNav : RecyclerView = itemView.findViewById(R.id.recyclerViewNav)


        init {

            itemView.setOnClickListener (this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition

            if(expanded_menu.isExpanded)
                expanded_menu.collapse()
            else
                expanded_menu.expand()

            val viewModel = ViewModel(context.applicationContext as Application)

            val partNumber = plupaInfoList[position].part_number

            val contentDetailsList = viewModel.getContentByPartTitle(partNumber)

            val plupaAdapter = PlupaAdapterDetails(context, contentDetailsList)

            recyclerViewNav.layoutManager = LinearLayoutManager(context)
            recyclerViewNav.setHasFixedSize(true)
            recyclerViewNav.adapter = plupaAdapter


        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.nav_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {


        val part_name = plupaInfoList[position].part_name
        val part_number = plupaInfoList[position].part_number

        val partText = part_name.substring(0,1).toUpperCase() + part_name.substring(1)

        holder.tvPlupaHeading.text = stripHtml("($part_number.) $partText")


    }

    fun capitalize(str: String): String {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1)
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



}