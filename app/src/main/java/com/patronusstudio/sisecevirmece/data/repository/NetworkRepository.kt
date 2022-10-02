package com.patronusstudio.sisecevirmece.data.repository

import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.model.LoginResponseModel
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
}