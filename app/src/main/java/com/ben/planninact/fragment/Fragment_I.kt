package com.ben.planninact.fragment

import android.R.attr.path
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ben.planninact.R
import com.ben.planninact.adapter.PlupaSearchAdapter
import com.ben.planninact.helper_class.PlupaPojo
import com.ben.planninact.room_persitence.ViewModel
import kotlinx.android.synthetic.main.fragment_i.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Fragment_I : Fragment() {


    private lateinit var plupaViewModel: ViewModel

    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_i, container, false)
        plupaViewModel = ViewModel(requireActivity().applicationContext as Application)

        recyclerView = rootView.findViewById(R.id.recyclerView)

        return rootView
    }

    override fun onStart() {
        super.onStart()

        CoroutineScope(Dispatchers.IO).launch {

            val sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE);
            val plupaQuery = sharedpreferences.getStringSet("plupaQuery", null)

            if (plupaQuery != null) {
                val list = ArrayList<String>(plupaQuery)

                val plupaDataList = ArrayList<PlupaPojo>()

                for (items in list){

                    val items1 = items
                    val id = items1.split("-")[0]
                    val type = items1.substring(id.length+1)

                    val plupaData = plupaViewModel.getPlupaDetailsById(id, type)

                    val plupaPojo = PlupaPojo(
                        plupaData.dbId, plupaData.dbHeading, plupaData.dbBody, type
                    )
                    plupaDataList.add(plupaPojo)

                }

                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    recyclerView.setHasFixedSize(true)

                    val plupaAdapter = PlupaSearchAdapter(
                        requireActivity(), plupaDataList
                    )
                    recyclerView.adapter = plupaAdapter
                }


            }

        }




    }


}