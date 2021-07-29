package com.ben.planninact.network_persitence

import retrofit2.Call
import retrofit2.http.GET


interface Interface {


    @GET("/api/v1/get_all_data")
    fun getPlupaDetails(): Call<DBScheduleDetails>

}