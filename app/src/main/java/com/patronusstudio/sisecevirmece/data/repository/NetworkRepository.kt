package com.patronusstudio.sisecevirmece.data.repository

import com.patronusstudio.sisecevirmece.data.model.*
import com.patronusstudio.sisecevirmece.data.objects.RetrofitObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NetworkRepository {

    suspend fun loginWithUsernamePass(loginRequestModel: LoginRequestModel): Response<LoginResponseModel> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.service.login(loginRequestModel)
        }
    }

    suspend fun register(userModelRegister: UserModelRegister): Response<UserModelResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.service.register(userModelRegister)
        }
    }

    suspend fun emailControl(email: String): Response<SampleResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.service.checkEmail(email)
        }
    }

    suspend fun usernameControl(username: String): Response<SampleResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.service.checkUsername(username)
        }
    }

    suspend fun getUserGameInfo(username: String):Response<UserInfoModelResponse>{
        return withContext(Dispatchers.IO){
            RetrofitObjects.service.getUserGameInfo(username)
        }
    }
}
