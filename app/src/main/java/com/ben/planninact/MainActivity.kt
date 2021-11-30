package com.ben.planninact

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ben.planninact.adapter.FirstScheduleAdapter
import com.ben.planninact.adapter.PlupaHeadingsAdapter
import com.ben.planninact.adapter.SecondScheduleAdapter
import com.ben.planninact.adapter.ThirdScheduleAdapter
import com.ben.planninact.fragment.Fragment_Home
import com.ben.planninact.fragment.Fragment_I
import com.ben.planninact.fragment.Fragment_Titles
import com.ben.planninact.helper_class.*
import com.ben.planninact.network_persitence.*
import com.ben.planninact.room_persitence.ViewModel
import com.ben.planninact.room_persitence.entity.*
import com.ben.planninact.room_persitence.entity.ThirdSchedule
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var plupaViewModel: ViewModel
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedpreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedpreferences = getSharedPreferences("Plupa", Context.MODE_PRIVATE);

        editor = sharedpreferences.edit()
        plupaViewModel = ViewModel(this.applicationContext as Application)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        getLocalPlupa()

        if (CheckInternet().isConnected(this)){
            checkData()
        }else{
            preparePlupaDetails()
        }

        tvPlupaContent.setOnClickListener {

            setTitleData("plupa_title")

        }
        tvFirstSchedule.setOnClickListener {

            setTitleData("first_schedule")

        }
        tvSecondSchedule.setOnClickListener {

            setTitleData("second_schedule")

        }
        tvThirdSchedule.setOnClickListener {

            setTitleData("third_schedule")

        }

    }

    private fun setTitleData(type: String) {

        val sharedpreferences = getSharedPreferences("Plupa", Context.MODE_PRIVATE);
        val editor: SharedPreferences.Editor = sharedpreferences.edit()

        editor.putString("part_title", type)
        editor.apply()

        replaceFragmenty(
            fragment = Fragment_Titles(),
            allowStateLoss = true,
            containerViewId = R.id.mainContent
        )

        drawerLayout.closeDrawer(GravityCompat.START)


    }

    private fun getLocalPlupa(){

        val jsonFileString = Formatter().getJsonDataFromAsset(this)
        if (jsonFileString != null) {

            val json = JSONObject(jsonFileString)
            val partDetails = json.getJSONArray("partDetails")
            val firstSchedule = json.getJSONArray("firstSchedule")
            val secondSchedule = json.getJSONArray("secondSchedule")
            val thirdSchedule = json.getJSONArray("thirdSchedule")

            //Part Title
            val dbPartTitleList = ArrayList<DbPartTitle>()
            for (i in 0 until partDetails.length()){

                val jsonObject = partDetails.getJSONObject(i)
                val id = jsonObject.getString("id")
                val partNumber = jsonObject.getString("part_number")
                val partName = jsonObject.getString("part_name")

                val dbPartDetailsList = ArrayList<DbPartDetails>()
                val jsonArrayContent = jsonObject.getJSONArray("content_description")
                for (j in 0 until jsonArrayContent.length()){

                    val jsonObjectContent = jsonArrayContent.getJSONObject(j)
                    val contentId = jsonObjectContent.getString("id")
                    val contentPartDescription = jsonObjectContent.getString("part_description")
                    val contentPartHeading = jsonObjectContent.getString("part_heading")
                    val contentPartId = jsonObjectContent.getString("part_id")

                    val dbPartDetails = DbPartDetails(contentId, contentPartDescription, contentPartHeading, contentPartId)
                    dbPartDetailsList.add(dbPartDetails)
                }

                val dbPartTitle = DbPartTitle(id, partNumber, partName, dbPartDetailsList)
                dbPartTitleList.add(dbPartTitle)
            }
            plupaViewModel.insertPartData(dbPartTitleList)

            //First Schedule
            val firstScheduleList = ArrayList<DbFirstSchedule>()
            for (i in 0 until firstSchedule.length()){

                val jsonObject = firstSchedule.getJSONObject(i)
                val id = jsonObject.getString("id")
                val part_name = jsonObject.getString("part_name")
                val content_description = jsonObject.getString("content_description")

                val dbFirstSchedule = DbFirstSchedule(id, part_name, content_description)
                firstScheduleList.add(dbFirstSchedule)

            }
            plupaViewModel.insertFirstSchedule(firstScheduleList)

            //Second Schedule
            val secondScheduleList = ArrayList<DbSecondSchedule>()
            for (i in 0 until secondSchedule.length()){

                val jsonObject = secondSchedule.getJSONObject(i)
                val id = jsonObject.getString("id")
                val part_name = jsonObject.getString("part_name")
                val content_description = jsonObject.getString("content_description")

                val dbSecondSchedule = DbSecondSchedule(id, part_name, content_description)
                secondScheduleList.add(dbSecondSchedule)

            }
            plupaViewModel.insertSecondSchedule(secondScheduleList)

            //Third Schedule
            val thirdScheduleList = ArrayList<DbThirdSchedule>()
            for (i in 0 until thirdSchedule.length()){

                val jsonObject = thirdSchedule.getJSONObject(i)
                val id = jsonObject.getString("id")
                val title = jsonObject.getString("title")
                val content_description = jsonObject.getString("content_description")

                val dbThirdSchedule = DbThirdSchedule(id, title, content_description)
                thirdScheduleList.add(dbThirdSchedule)

            }
            plupaViewModel.insertThirdSchedule(thirdScheduleList)



        }

    }

    private fun preparePlupaDetails() {

        plupaViewModel.titleList.observe(this@MainActivity, { titleList: List<PartDetailsTitle> ->
            this@MainActivity.prepareRecyclerView(titleList)
        })

        plupaViewModel.firstScheduleList.observe(this@MainActivity, { firstScheduleList: List<FirstSchedule> ->
            this@MainActivity.prepareFirstSchedule(firstScheduleList)
        })

        plupaViewModel.secondScheduleList.observe(this@MainActivity, { secondScheduleList: List<SecondSchedule> ->
            this@MainActivity.prepareSecondSchedule(secondScheduleList)
        })

        plupaViewModel.thirdScheduleList.observe(this@MainActivity, { thirdScheduleList: List<ThirdSchedule> ->
            this@MainActivity.prepareThirdSchedule(thirdScheduleList)
        })

        CoroutineScope(Dispatchers.IO).launch {
//            val titleList = plupaViewModel.getTitles()
//            val firstScheduleList = plupaViewModel.getFirstSchedule()
//            val secondScheduleList = plupaViewModel.getSecondSchedule()
//            val thirdScheduleList = plupaViewModel.getThirdSchedule()

//            prepareRecyclerView(titleList)
//            prepareFirstSchedule(firstScheduleList)
//            prepareSecondSchedule(secondScheduleList)
//            prepareThirdSchedule(thirdScheduleList)
        }


    }


    override fun onStart() {
        super.onStart()


        preparePlupaDetails()

        val page_detail_splash =intent.getStringExtra("page_detail")
        if (page_detail_splash != null && page_detail_splash == "true"){
            drawerLayout.openDrawer(GravityCompat.START)
        }else{
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        replaceFragmenty(
            fragment = Fragment_Home(),
            allowStateLoss = true,
            containerViewId = R.id.mainContent
        )


    }

    private fun prepareThirdSchedule(thirdScheduleList: List<ThirdSchedule>) {
        CoroutineScope(Dispatchers.Main).launch {

            recyclerViewThirdSchedule.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewThirdSchedule.setHasFixedSize(true)

            val plupaAdapter = ThirdScheduleAdapter(
                this@MainActivity, thirdScheduleList
            )
            recyclerViewThirdSchedule.adapter = plupaAdapter
        }

    }

    private fun prepareSecondSchedule(secondScheduleList: List<SecondSchedule>) {

        CoroutineScope(Dispatchers.Main).launch {

            recyclerViewSecondSchedule.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewSecondSchedule.setHasFixedSize(true)

            val plupaAdapter = SecondScheduleAdapter(
                this@MainActivity, secondScheduleList
            )
            recyclerViewSecondSchedule.adapter = plupaAdapter
        }

    }

    private fun prepareFirstSchedule(firstScheduleList: List<FirstSchedule>) {

        CoroutineScope(Dispatchers.Main).launch {

            recyclerViewFirstSchedule.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewFirstSchedule.setHasFixedSize(true)

            val plupaAdapter = FirstScheduleAdapter(
                this@MainActivity, firstScheduleList
            )
            recyclerViewFirstSchedule.adapter = plupaAdapter
        }

    }

    private fun prepareRecyclerView(titleList: List<PartDetailsTitle>) {

        CoroutineScope(Dispatchers.Main).launch {

            recyclerViewItems.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewItems.setHasFixedSize(true)

            val plupaAdapter = PlupaHeadingsAdapter(
                this@MainActivity, titleList
            )
            recyclerViewItems.adapter = plupaAdapter
        }




    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun checkData() {

        val apiService = RetrofitBuilder.getRetrofit("http://192.168.81.34:8082")
            .create(Interface::class.java)

        //Get Details
        val apiInterface1 = apiService.getPlupaDetails()
        apiInterface1.enqueue(object : Callback<DBScheduleDetails> {
            override fun onResponse(
                call: Call<DBScheduleDetails>,
                response: Response<DBScheduleDetails>
            ) {

                if (response.isSuccessful) {

                    val responseDetails = response.body()
                    if (responseDetails != null){

                        val partDetailsList = responseDetails.partDetails
                        val firstScheduleList = responseDetails.firstSchedule
                        val secondScheduleList = responseDetails.secondSchedule
                        val thirdScheduleList = responseDetails.thirdSchedule

                        plupaViewModel.insertPartData(partDetailsList)
                        plupaViewModel.insertFirstSchedule(firstScheduleList)
                        plupaViewModel.insertSecondSchedule(secondScheduleList)
                        plupaViewModel.insertThirdSchedule(thirdScheduleList)

                    }

                }
            }

            override fun onFailure(call: Call<DBScheduleDetails>, t: Throwable) {


                Log.e("-*-*error ", t.localizedMessage)
            }

        })

        preparePlupaDetails()
    }




    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate menu with items using MenuInflator
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)

        val searchViewItem = menu.findItem(R.id.app_bar_search)
        val searchView : SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setQueryHint("Start typing..")
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    val plupaQueryList = plupaViewModel.getPlupaResults(query)

                    replaceFragmenty(
                        fragment = Fragment_I(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                    )



                    addPlupaShared(plupaQueryList,query)

                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    val plupaQueryList = plupaViewModel.getPlupaResults(newText)
                    replaceFragmenty(
                        fragment = Fragment_I(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                    )

                    addPlupaShared(plupaQueryList,newText)

                    return false
                }
            })

        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.download->{

                if (CheckInternet().isConnected(this)){
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://kenyalaw.org/kl/fileadmin/pdfdownloads/Acts/2019/PhysicalandLandUsePlanningAct_No13of2019.pdf"))
                    startActivity(browserIntent)
                }else{
                    Toast.makeText(this, "Make sure you're connected to the internet", Toast.LENGTH_LONG).show()
                }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addPlupaShared(plupaQueryList: List<PlupaPojo>, search_results: String) {

        val idList = ArrayList<String>()

        for (type in plupaQueryList){

            val id = type.dbId
            val dbType = type.dbType
            val dbName = "$id-$dbType"
            idList.add(dbName)
        }

        val setArrayList = HashSet<String>()
        setArrayList.addAll(idList)

        editor.putStringSet("plupaQuery", setArrayList)
        editor.apply()


        editor.putString("search_results", search_results)
        editor.apply()

    }
}

