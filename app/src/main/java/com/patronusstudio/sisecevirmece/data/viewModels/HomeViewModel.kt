package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository:NetworkRepository,
    private val localRepository:LocalRepository,
): ViewModel() {

    suspend fun getUserGameInfo(username:String){
        val result = networkRepository.getUserGameInfo(username)
        result.body()
    }
}