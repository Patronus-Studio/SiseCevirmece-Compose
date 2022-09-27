package com.patronusstudio.sisecevirmece.data.objects

import com.patronusstudio.sisecevirmece.data.interfaces.BottleService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObjects {
    private val retrofit =  Retrofit.Builder()
        .baseUrl("https://bottleflip.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    val service = retrofit.create(BottleService::class.java)
}