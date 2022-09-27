package com.patronusstudio.sisecevirmece.data.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.model.LoginResponseModel
import com.patronusstudio.sisecevirmece.data.objects.DataStoreObjects.dataStore
import com.patronusstudio.sisecevirmece.data.objects.RetrofitObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Response

class Repository {

    val token = stringPreferencesKey("user_token")

    suspend fun loginWithUsernamePass(loginRequestModel: LoginRequestModel): Response<LoginResponseModel> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.service.login(loginRequestModel)
        }
    }

    fun getUserTokenOnLocal(context: Context): Flow<String?> {
        return context.dataStore.data.map {
            it[token]
        }
    }

    suspend fun setUserTokenOnLocal(context: Context, usertoken:String) {
        context.dataStore.edit {
            it[token] = usertoken
        }
    }
}
