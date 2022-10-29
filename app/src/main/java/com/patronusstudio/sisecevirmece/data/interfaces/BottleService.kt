package com.patronusstudio.sisecevirmece.data.interfaces

import com.patronusstudio.sisecevirmece.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface BottleService {

    @POST("/login")
    suspend fun login(@Body loginModel: LoginRequestModel): Response<LoginResponseModel>

    @POST("/register")
    suspend fun register(@Body registerModel: UserModelRegister): Response<UserModelResponse>

    @POST("/emailControl")
    suspend fun checkEmail(@Query("email") email: String): Response<SampleResponse>

    @POST("/usernameControl")
    suspend fun checkUsername(@Query("username") username: String): Response<SampleResponse>

    @GET("userGameInfo/getUserGameInfo")
    suspend fun getUserGameInfo(@Query("username") username: String) : Response<UserInfoModelResponse>
}