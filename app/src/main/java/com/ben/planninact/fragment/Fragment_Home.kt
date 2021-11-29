package com.ben.planninact.fragment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.ben.planninact.R
import com.ben.planninact.helper_class.Formatter
import com.ben.planninact.room_persitence.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


class Fragment_Home : Fragment() {


    private lateinit var plupaViewModel: ViewModel
    private lateinit var list_item: ListView
    private lateinit var sharedpreferences :SharedPreferences
    private lateinit var fabPrev :FloatingActionButton
    private lateinit var fabNext :FloatingActionButton

//    private lateinit var swipeToRefreshLayout: SwipeRefreshLayout
    private var previousScrollY: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE)

        plupaViewModel = ViewModel(requireActivity().applicationContext as Application)

//        rootView.scrollView.viewTreeObserver.addOnScrollChangedListener {
//            val scrollY: Int = rootView.scrollView.scrollY // For ScrollView
//            val scrollX: Int = rootView.scrollView.scrollX // For HorizontalScrollView
//
//            if (rootView.scrollView != null){
//
//                if (rootView.scrollView.getChildAt(0).bottom
//                    > (rootView.scrollView.height + rootView.scrollView.scrollY)){
//                    rootView.linearLayout.visibility = View.VISIBLE
//                }else{
//                    rootView.linearLayout.visibility = View.GONE
//                }
//
//            }


//            if (scrollY > 0){
//                rootView.linearLayout.visibility = View.VISIBLE
//            }else if (scrollY < 0){
//                rootView.linearLayout.visibility = View.GONE
//            }

            // DO SOMETHING WITH THE SCROLL COORDINATES
//        }


//        list_item = rootView.findViewById(R.id.list_item)
//        swipeToRefreshLayout = rootView.findViewById(R.id.swipeRefresh)

//        rootView.scrollView.setOnTouchListener(object : OnSwipeTouchListener(requireActivity()){
//            override fun onSwipeRight() {
//
//                val intent = Intent(requireActivity(), MainActivity::class.java)
//                startActivity(intent)
//                requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//
//                updatePageNumbers("sub")
//
//            }
//
//            override fun onSwipeLeft() {
//
//                val intent = Intent(requireActivity(), MainActivity::class.java)
//                startActivity(intent)
//                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                updatePageNumbers("add")
//
//
//            }
//
//
//        })

//        swipeToRefreshLayout.setOnRefreshListener {
//            Handler().postDelayed({
//                swipeToRefreshLayout.isRefreshing = false
//
//                if (CheckInternet().isConnected(requireActivity())){
//
//                    val intent = Intent(requireActivity(), MainActivity::class.java)
//                    startActivity(intent)
//
//                }else{
//                    Toast.makeText(requireActivity(), "Make sure you're connected to the internet", Toast.LENGTH_LONG).show()
//                }
//
//            }, 4000)
//        }
//
//        swipeToRefreshLayout.setColorSchemeResources(
//            R.color.teal_700,
//            R.color.teal_200,
//            R.color.purple_500,
//            R.color.purple_200,
//        )
        fabNext = rootView.findViewById(R.id.fabNext)
        fabPrev = rootView.findViewById(R.id.fabPrev)

        fabNext.setOnClickListener {

            setNewPage("next")

        }

        fabPrev.setOnClickListener {

            setNewPage("prev")


        }


        return rootView
    }

    private fun setNewPage(nav: String) {
        val sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE);
        val editor: SharedPreferences.Editor = sharedpreferences.edit()

        val plupaId = sharedpreferences.getString("plupa_id", null)
        if (plupaId != null){
            val part_type = sharedpreferences.getString("part_type", null)
            var plupa_id = plupaId.toInt()

            if (part_type == "plupa_content"){

                if (plupa_id in 2..92){

                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()


                    }

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }


                }else if(plupa_id == 1){
                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id == 93){

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                    if (nav == "next"){

                        val newpage = 1

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }

            }
            if (part_type == "first_schedule"){

                if (plupa_id == 1){
                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }
                    if (nav == "prev"){

                        val newpage = 93

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id in 2..3){

                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()


                    }

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id == 4){

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                    if (nav == "next"){

                        val newpage = 1

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }

            }

            if (part_type == "second_schedule"){

                if (plupa_id == 1){
                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }
                    if (nav == "prev"){

                        val newpage = 4

                        editor.putString("part_type", "first_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id == 2 ){

                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()


                    }

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id == 3){

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                    if (nav == "next"){

                        val newpage = 1

                        editor.putString("part_type", "third_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }

            }
            if (part_type == "third_schedule"){

                if (plupa_id == 1){
                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "third_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                    if (nav == "prev"){

                        val newpage = 3

                        editor.putString("part_type", "second_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id in 2..6){

                    if (nav == "next"){

                        val newpage = plupa_id + 1

                        editor.putString("part_type", "third_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()


                    }

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "third_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }else if (plupa_id == 7){

                    if (nav == "prev"){

                        val newpage = plupa_id - 1

                        editor.putString("part_type", "third_schedule")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                    if (nav == "next"){

                        val newpage = 1

                        editor.putString("part_type", "plupa_content")
                        editor.putString("plupa_id", newpage.toString())
                        editor.apply()

                    }

                }

            }

            setPageContent()


        }




    }




    override fun onStart() {
        super.onStart()

        val editor: SharedPreferences.Editor = sharedpreferences.edit()

        editor.putBoolean("page_detail", false)
        editor.apply();

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
                imageView.visibility = View.GONE
            }else{

                imageView.visibility = View.VISIBLE

            }

//            prepareRecyclerView(titleList)
//            prepareFirstSchedule(firstScheduleList)
//            prepareSecondSchedule(secondScheduleList)
//            prepareThirdSchedule(thirdScheduleList)
        }

        setPageContent()



//        val plupaId = sharedpreferences.getString("plupaId", null)
//
//        plupaViewModel = ViewModel(requireActivity().applicationContext as Application)
//
//
//        if (plupaId != null){
//            val plupaInfoList = plupaViewModel.getPlupaDataById(plupaId.toString())
//
//            val partDesc = ArrayList<String>()
//
//            var data = ""
//            var dataHeading = ""
//            var dataPartId = ""
//            for(items in plupaInfoList){
//                data = data + "\n" + Formatter().stripHtml(items.part_description)
//                partDesc.add(data)
//
//                dataHeading = items.part_heading
//
//                dataPartId = "PART ${items.part_id} - ${plupaViewModel.getPlupaByPartNumber(items.part_id).part_name}"
//
//            }
//
////            val adapter: ArrayAdapter<*> = ArrayAdapter(requireActivity(), R.layout.activity_listview, partDesc)
////            list_item.adapter = adapter;
//
//            tvPlupaDetails.text = Formatter().stripHtml(dataHeading)
//            tvPartHeading.text = Formatter().stripHtml(dataPartId)
//            tvPlupaData.text = Formatter().stripHtml(data)
//
//        }else{
//
//            if (CheckInternet().isConnected(requireActivity())){
//
//                val sharedpreferences = requireActivity().getSharedPreferences("Plupa", Context.MODE_PRIVATE);
//                val editor: SharedPreferences.Editor = sharedpreferences.edit()
//
//                editor.putString("plupaId", "3");
//                editor.apply();
//
//            }
//
//
//
//        }


    }

    private fun setPageContent() {

        val plupaId = sharedpreferences.getString("plupa_id", null)
        if (plupaId != null){

            val part_type = sharedpreferences.getString("part_type", null)

            var plupa_id = plupaId.toInt()
            if (part_type == "plupa_content"){

                val plupaData = plupaViewModel.getContentByPartId(plupa_id)
                val shortTitle = plupaData.part_heading
                val partDescription = plupaData.part_description

                setDataText(shortTitle, partDescription)


            }
            if (part_type == "first_schedule"){

                val plupaData = plupaViewModel.getFirstScheduleByPartId(plupa_id)
                val shortTitle = plupaData.part_name
                val partDescription = plupaData.content_description

                setDataText("Part $shortTitle", partDescription)


            }
            if (part_type == "second_schedule"){

                val plupaData = plupaViewModel.getSecondScheduleByPartId(plupa_id)
                val shortTitle = plupaData.part_name
                val partDescription = plupaData.content_description
                var partName = ""

                when (shortTitle) {
                    "A" -> {
                        partName =
                            "Part $shortTitle \n MATTERS WHICH MAY BE DEALT WITH IN A LOCAL PHYSICAL AND LAND USE DEVELOPMENT PLAN"
                    }
                    "B" -> {
                        partName =
                            "Part $shortTitle \n CONTENTS OF SURVEY REPORT"
                    }
                    "C" -> {
                        partName =
                            "Part $shortTitle \n CONTENT FOR RENEWAL AND RE-DEVELOPMENT PLAN"
                    }
                }
                setDataText(partName, partDescription)

            }
            if (part_type == "third_schedule"){

                val plupaData = plupaViewModel.getThirdScheduleByPartId(plupa_id)
                val shortTitle = plupaData.title
                val partDescription = plupaData.content_description

                setDataText(shortTitle, partDescription)


            }


        }


    }

    private fun setDataText(shortTitle: String, partDescription: String) {

        val formattedHtmlTitle = HtmlFormatter.formatHtml(
            HtmlFormatterBuilder().setHtml(shortTitle.toString())
                .setImageGetter(HtmlResImageGetter(tvPlupaDetails.context))
        )
        tvPlupaDetails.text = formattedHtmlTitle

        val formattedHtmlContent = HtmlFormatter.formatHtml(
            HtmlFormatterBuilder().setHtml(partDescription.toString())
                .setImageGetter(HtmlResImageGetter(tvPlupaData.context))
        )
        tvPlupaData.text = formattedHtmlContent

        val searchResults = sharedpreferences.getString("search_results", null)

        if(searchResults != null){

            val titleText = tvPlupaDetails.text.toString()
            val contentText = tvPlupaData.text.toString()

            val highLightedTitle = getFormattedText(titleText, searchResults)
            val highLightedDesc = getFormattedText(contentText, searchResults)

            if (highLightedTitle != null){
                tvPlupaDetails.text = highLightedTitle
            }
            if (highLightedDesc != null){
                tvPlupaData.text = highLightedDesc
            }



        }


    }

    private fun getFormattedText(oldText: String, highLightText:String):Spannable?{

        return try {

            val x = oldText.toLowerCase().indexOf(highLightText.toLowerCase())
            val y = x + highLightText.length

            val span: Spannable = SpannableString(oldText)
            span.setSpan(BackgroundColorSpan(Color.YELLOW), x, y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            Log.e("*-*-*3", span.toString())

            span

        }catch (e: Exception){
            Log.e("*-*-*32", e.toString())

            null
        }



    }


}