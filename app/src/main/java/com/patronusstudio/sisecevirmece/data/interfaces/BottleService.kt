package com.patronusstudio.sisecevirmece.data.interfaces

import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.model.LoginResponseModel
import com.patronusstudio.sisecevirmece.data.model.UserModelRegister
import com.patronusstudio.sisecevirmece.data.model.UserModelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BottleService {

    @POST("/login")
    suspend fun login(@Body loginModel: LoginRequestModel): Response<LoginResponseModel>

    @POST("/register")
    suspend fun register(@Body registerModel: UserModelRegister): Response<UserModelResponse>
}