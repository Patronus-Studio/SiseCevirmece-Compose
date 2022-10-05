package com.patronusstudio.sisecevirmece.data.objects

import com.patronusstudio.sisecevirmece.data.interfaces.BottleService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObjects {
    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://bottleflip.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    val service = retrofit.create(BottleService::class.java)
}