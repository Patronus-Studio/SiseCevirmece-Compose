package com.patronusstudio.sisecevirmece2.data.objects

import com.patronusstudio.sisecevirmece2.data.interfaces.BottleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitObjects {
    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()

    @Singleton
    @Provides
    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://bottleflip.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getApi():BottleService{
        return getRetrofit().create(BottleService::class.java)
    }
}