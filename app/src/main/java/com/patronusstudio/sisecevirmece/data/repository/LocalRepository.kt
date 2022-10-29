package com.patronusstudio.sisecevirmece.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.patronusstudio.sisecevirmece.data.objects.DataStoreObjects.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepository @Inject constructor() {

    private val token = stringPreferencesKey("user_token")

    fun getUserTokenOnLocal(context: Context): Flow<String?> {
        return context.dataStore.data.map {
            it[token]
        }
    }

    suspend fun setUserTokenOnLocal(context: Context, usertoken: String) {
        context.dataStore.edit {
            it[token] = usertoken
        }
    }

    suspend fun removeUserToken(context: Context) {
        context.dataStore.edit {
            it.remove(token)
        }
    }
}