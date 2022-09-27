package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel() : ViewModel() {

    private val repository by lazy { Repository() }

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
                repository.getUserTokenOnLocal(context)
            }
            user_token.firstOrNull()?.let {
                _token.value = it
            }
            isAnimationShow.value = false
        }
    }

    suspend fun setUserToken(context: Context){
        repository.setUserTokenOnLocal(context,_token.value)
    }

    fun loginWithEmailPass() {
        CoroutineScope(Dispatchers.Main).launch {
            setAnimShow(true)
           val loginRequestModel = LoginRequestModel(username.value, userPassword.value)
            val result = repository.loginWithUsernamePass(loginRequestModel)
            if(result.body() != null && result.isSuccessful){
                if(result.body()!!.status == HttpStatusEnum.OK){
                    _token.value = result.body()!!.token
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