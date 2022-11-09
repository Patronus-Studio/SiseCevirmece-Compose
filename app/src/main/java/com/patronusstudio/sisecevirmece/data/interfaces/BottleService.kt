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

    @POST("userGameInfo/getUserGameInfo")
    suspend fun getUserGameInfo(@Query("authToken") authToken: String) : Response<UserInfoModelResponse>

    @GET("avatar/getAvatars")
    suspend fun getAvatars(@Query("username") username: String):Response<AvatarResponseModel>

    @GET("level/getAllLevel")
    suspend fun getAllLevel():Response<LevelResponseModel>

    @GET("package/getAllPackageCategories")
    suspend fun getAllPackageCategories():Response<PackageCategoryResponseModel>
}