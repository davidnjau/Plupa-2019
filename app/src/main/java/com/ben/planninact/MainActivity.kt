package com.ben.planninact

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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
import com.ben.planninact.helper_class.CheckInternet
import com.ben.planninact.helper_class.NavData
import com.ben.planninact.network_persitence.DBScheduleDetails
import com.ben.planninact.network_persitence.Interface
import com.ben.planninact.network_persitence.RetrofitBuilder
import com.ben.planninact.room_persitence.ViewModel
import com.ben.planninact.room_persitence.entity.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        if (CheckInternet().isConnected(this)){
            checkData()
        }else{
            preparePlupaDetails()
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

        drawerLayout.openDrawer(GravityCompat.START)

        preparePlupaDetails()

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
        searchView.setQueryHint("Start typing..");
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {

                    val plupaQueryList = plupaViewModel.getPlupaResults(query)

                    replaceFragmenty(
                        fragment = Fragment_I(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                    )

                    addPlupaShared(plupaQueryList)

                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {

                    val plupaQueryList = plupaViewModel.getPlupaResults(newText)
                    replaceFragmenty(
                        fragment = Fragment_I(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                    )

                    addPlupaShared(plupaQueryList)

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

    private fun addPlupaShared(plupaQueryList: List<String>) {
        val setArrayList = HashSet<String>()
        setArrayList.addAll(plupaQueryList)

        editor.putStringSet("plupaQuery", setArrayList)
        editor.apply()

    }
}

