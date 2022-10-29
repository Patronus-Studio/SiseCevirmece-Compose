package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository

class HomeViewModel: ViewModel() {

    private val networkRepository by lazy { NetworkRepository() }
    private val localRepository by lazy { LocalRepository() }

    suspend fun getUserGameInfo(username:String){
        val result = networkRepository.getUserGameInfo(username)
        result.body()
    }
}