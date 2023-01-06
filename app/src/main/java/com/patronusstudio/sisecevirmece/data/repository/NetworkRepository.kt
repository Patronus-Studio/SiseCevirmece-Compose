package com.patronusstudio.sisecevirmece.data.repository

import com.patronusstudio.sisecevirmece.data.model.*
import com.patronusstudio.sisecevirmece.data.objects.RetrofitObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor() {

    suspend fun loginWithUsernamePass(loginRequestModel: LoginRequestModel): Response<LoginResponseModel> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().login(loginRequestModel)
        }
    }

    suspend fun register(userModelRegister: UserModelRegister): Response<UserModelResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().register(userModelRegister)
        }
    }

    suspend fun emailControl(email: String): Response<SampleResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().checkEmail(email)
        }
    }

    suspend fun usernameControl(username: String): Response<SampleResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().checkUsername(username)
        }
    }

    suspend fun getUserGameInfo(username: String): Response<UserInfoModelResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().getUserGameInfo(username)
        }
    }

    suspend fun getAvatars(username: String):Response<AvatarResponseModel>{
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().getAvatars(username)
        }
    }

    suspend fun getLevels():Response<LevelResponseModel>{
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().getAllLevel()
        }
    }

}
