package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.AvatarModel
import com.patronusstudio.sisecevirmece.data.model.AvatarResponseModel
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

    private val _errorMessage = MutableStateFlow("")
    val errorMessage :StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _userGameInfoModel = MutableStateFlow<UserInfoModel?>(null)
    val userGameInfoModel : StateFlow<UserInfoModel?> = _userGameInfoModel

    private val _avatars = MutableStateFlow<List<AvatarModel>?>(null)
    val avatar : StateFlow<List<AvatarModel>?> = _avatars

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

    fun getCurrentAvatar(): AvatarModel? {
        return _avatars.value?.firstOrNull{
            it.id.toString() == _userGameInfoModel.value?.currentAvatar
        }
    }

    suspend fun getAvatars(){
        _isLoading.value = true
        val avatarsResponse = networkRepository.getAvatars(_userGameInfoModel.value?.username ?: "")
        if(avatarsResponse.isSuccessful.not() || avatarsResponse.body()?.status != HttpStatusEnum.OK){
            _errorMessage.value = avatarsResponse.body()?.message ?: "Avatarlar çekilirken bir hata oluştu."
            _isLoading.value = false
            return
        }
        _avatars.value = avatarsResponse.body()!!.data
        _isLoading.value = false
    }
}