package com.ben.planninact.fragment

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ben.planninact.MainActivity
import com.ben.planninact.R
import com.ben.planninact.adapter.TitleScheduleAdapter
import com.ben.planninact.room_persitence.ViewModel
import kotlinx.android.synthetic.main.fragment_home.imageView
import kotlinx.android.synthetic.main.fragment_titles.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Fragment_Titles : Fragment() {


    private lateinit var plupaViewModel: ViewModel
    private lateinit var sharedpreferences :SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_titles, container, false)

        sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE)

        plupaViewModel = ViewModel(requireActivity().applicationContext as Application)




        return rootView
    }



    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.IO).launch {
            val titleList = plupaViewModel.getTitles()
            val firstScheduleList = plupaViewModel.getFirstSchedule()
            val secondScheduleList = plupaViewModel.getSecondSchedule()
            val thirdScheduleList = plupaViewModel.getThirdSchedule()

            if (
                titleList.isNotEmpty() &&
                firstScheduleList.isNotEmpty() &&
                secondScheduleList.isNotEmpty() &&
                thirdScheduleList.isNotEmpty()){

                val part_title = sharedpreferences.getString("part_title", null)
                val titleDataList = ArrayList<String>()
                val idList = ArrayList<String>()
                var titleName = ""

                when (part_title) {
                    "plupa_title" -> {

                        for (items in titleList){

                            val title = items.part_name
                            val part_number = items.part_number
                            val ids = items.id.toString()
                            idList.add(ids)

                            val title_data = "$part_number $title"
                            titleDataList.add(title_data)

                        }
                        titleName = "Plupa Content"

                    }
                    "first_schedule" -> {

                        for (items in firstScheduleList){

                            val title = items.part_name

                            val ids = items.id.toString()
                            idList.add(ids)

                            val title_data = "Part $title"
                            titleDataList.add(title_data)

                        }
                        titleName = "First Schedule : \nContents of National, Inter- County and County Physical and Land Use Development Plans. "

                    }
                    "second_schedule" -> {

                        for (items in secondScheduleList){

                            val title = items.part_name

                            val ids = items.id.toString()
                            idList.add(ids)

                            val title_data = "Part $title"
                            titleDataList.add(title_data)

                        }
                        titleName = "Second Schedule : \nContents of Local Physical and Land Use Development Plans. "

                    }
                    "third_schedule" -> {

                        for (items in thirdScheduleList){

                            val title = items.title
                            val ids = items.id.toString()
                            idList.add(ids)
                            titleDataList.add(title)

                        }
                        titleName = "Third Schedule: \nDevelopment Control. "

                    }
                    else -> {

                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)

                    }
                }

                prepareRecyclerView(titleDataList, idList, titleName)

                imageView.visibility = View.GONE
            }else{

                imageView.visibility = View.VISIBLE

            }


        }



    }

    private fun prepareRecyclerView(
        titleDataList: ArrayList<String>,
        idList: ArrayList<String>,
        titleName: String
    ) {

        CoroutineScope(Dispatchers.Main).launch {

            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.setHasFixedSize(true)

            val plupaAdapter = TitleScheduleAdapter(
                requireActivity(), titleDataList, idList
            )
            recyclerView.adapter = plupaAdapter

            tvPlupaTitle.text = titleName
        }

    }


}