package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository) : ViewModel() {


    private val _token = MutableStateFlow("")
    val token = _token.asStateFlow()

    val username = MutableStateFlow("")
    val userPassword = MutableStateFlow("")
    val isPasswordTrailLocked = MutableStateFlow(true)
    val isAnimationShow = MutableStateFlow(false)
    val isThereError = MutableStateFlow(Pair(false,""))

    fun setUserPassword(password: String) {
        userPassword.value = password
    }

    fun setUsername(username:String){
        this.username.value = username
    }

    fun setTrailIconClicked(boolean: Boolean) {
        isPasswordTrailLocked.value = boolean
    }


    fun userTokenControl(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            isAnimationShow.value = true
            val user_token =withContext(Dispatchers.IO){
                localRepository.getUserTokenOnLocal(context)
            }
            user_token.firstOrNull()?.let {
                _token.value = it
            }
            isAnimationShow.value = false
        }
    }

    suspend fun setUserToken(context: Context){
        localRepository.setUserTokenOnLocal(context,_token.value)
    }

    fun loginWithEmailPass() {
        CoroutineScope(Dispatchers.Main).launch {
            setAnimShow(true)
            val loginRequestModel = LoginRequestModel(username.value, userPassword.value)
            val result = networkRepository.loginWithUsernamePass(loginRequestModel)
            if(result.body() != null && result.isSuccessful){
                if(result.body()!!.status == HttpStatusEnum.OK){
                    _token.value = result.body()!!.message.toString()
                }
                else{
                    isThereError.value = Pair(true,result.body()?.message ?: "Bir hatayla karşılaşıldı.")
                }
            }
            else isThereError.value = Pair(true, "Reuqest hatalı oldu .")
            setAnimShow(false)
        }
    }

    fun set_tokenEmpty(){
        _token.value = ""
    }

    fun setAnimShow(boolean: Boolean) {
        isAnimationShow.value = boolean
    }
}