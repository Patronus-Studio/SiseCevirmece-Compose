package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.DataFetchStatus
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SplashScreenViewModel : ViewModel() {

    private val repository by lazy { LocalRepository() }

    private val _userToken: MutableStateFlow<String?> = MutableStateFlow(null)
    val userToken: StateFlow<String?> get() = _userToken

    private val _dataFetchStatus = MutableStateFlow(DataFetchStatus.INIT)
    val dataFetchStatus: StateFlow<DataFetchStatus> get() = _dataFetchStatus

    suspend fun getToken(mContext: Context) {
        withContext(Dispatchers.Main){
            _dataFetchStatus.value = DataFetchStatus.FETCHING
        }
        val result = withContext(Dispatchers.IO){
            repository.getUserTokenOnLocal(mContext)
        }
        withContext(Dispatchers.Main) {
            _userToken.value = result.firstOrNull()
            _dataFetchStatus.value = DataFetchStatus.FINISHED
        }
    }
}