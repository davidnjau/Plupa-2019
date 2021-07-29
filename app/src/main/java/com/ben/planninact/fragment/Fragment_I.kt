package com.ben.planninact.fragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ben.planninact.R
import com.ben.planninact.adapter.PlupaSearchAdapter
import com.ben.planninact.helper_class.PlupaPojo
import com.ben.planninact.room_persitence.ViewModel
import kotlinx.android.synthetic.main.fragment_i.*


class Fragment_I : Fragment() {


    private lateinit var plupaViewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_i, container, false)
        plupaViewModel = ViewModel(requireActivity().applicationContext as Application)


        return rootView
    }

    override fun onStart() {
        super.onStart()

        val sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE);
        val plupaQuery = sharedpreferences.getStringSet("plupaQuery", null)

        val list = ArrayList<String>(plupaQuery)

        if (plupaQuery != null) {

            val plupaDataList = ArrayList<PlupaPojo>()

            for (ids in list){

                val plupaData = plupaViewModel.getPlupaDetailsById(ids)

                val plupaPojo = PlupaPojo(
                    plupaData.id.toString(), plupaData.part_heading,
                    plupaData.part_description, plupaData.part_id
                )
                plupaDataList.add(plupaPojo)


            }

            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.setHasFixedSize(true)

            val plupaAdapter = PlupaSearchAdapter(
                requireActivity(), plupaDataList
            )
            recyclerView.adapter = plupaAdapter
        }


    }


}