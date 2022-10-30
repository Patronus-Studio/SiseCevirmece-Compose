package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.UserInfoModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository:NetworkRepository,
    private val localRepository:LocalRepository,
): ViewModel() {

    private val _loginError = MutableStateFlow("")
    val loginError : StateFlow<String> = _loginError

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _userGameInfoModel = MutableStateFlow<UserInfoModel?>(null)
    val userGameInfoModel : StateFlow<UserInfoModel?> = _userGameInfoModel

    suspend fun getUserGameInfo(authToken:String){
        _isLoading.value = true
        val result = networkRepository.getUserGameInfo(authToken)
        if(result.body() == null || result.body()!!.status != HttpStatusEnum.OK){
            _isLoading.value = false
            _loginError.value = result.body()?.message ?: "Kullanıcı bilgisi çekilirken bir hata oluştu. Tekrar giriş yapın."
            return
        }
        _userGameInfoModel.value = result.body()!!.data
        _isLoading.value = false
    }

    suspend fun clearAuthToken(mContext:Context){
        localRepository.removeUserToken(mContext)
    }
}