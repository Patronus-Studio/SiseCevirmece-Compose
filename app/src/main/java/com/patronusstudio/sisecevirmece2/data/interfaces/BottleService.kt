package com.patronusstudio.sisecevirmece2.data.interfaces

import com.patronusstudio.sisecevirmece2.data.model.*
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
    suspend fun getUserGameInfo(@Query("authToken") authToken: String): Response<UserInfoModelResponse>

    @GET("avatar/getAvatars")
    suspend fun getAvatars(@Query("username") username: String): Response<AvatarResponseModel>

    @GET("level/getAllLevel")
    suspend fun getAllLevel(): Response<LevelResponseModel>

    @GET("package/getAllPackageCategories")
    suspend fun getAllPackageCategories(): Response<PackageCategoryResponseModel>

    @GET("package/getPackageByCategoryName")
    suspend fun getPackageByCategoryName(@Query("packageCategory") packageCategory: Int): Response<PackageResponseModel>

    @POST("userGameInfo/updateCurrentAvatar")
    suspend fun updateCurrentAvatar(
        @Query("username") username: String,
        @Query("currentAvatar") currentAvatar: Int
    )

    @POST("package/updatePackageNumberOfDownload")
    suspend fun updatePackageNumberOfDownload(@Query("packageId") packageId: Int): Response<SampleResponse>

    @POST("userComment/setUserComment")
    suspend fun addNewComment(@Body userCommentRequest: UserCommentRequest): Response<SampleResponse>
}