package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.LoginRequestModel
import com.patronusstudio.sisecevirmece.data.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository by lazy { Repository() }

    val token = MutableStateFlow("")
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

    fun loginWithEmailPass() {
        CoroutineScope(Dispatchers.Main).launch {
            setAnimShow(true)
           // val loginRequestModel = LoginRequestModel(userEmail.value, userPassword.value)
            val loginRequestModel = LoginRequestModel()
            val result = repository.loginWithUsernamePass(loginRequestModel)
            if(result.body() != null && result.isSuccessful){
                if(result.body()!!.status == HttpStatusEnum.OK){
                    token.value = result.body()!!.token
                }
                else{
                    isThereError.value = Pair(true,result.body()?.message ?: "Bir hatayla karşılaşıldı.")
                }
            }
            else isThereError.value = Pair(true, "Reuqest hatalı oldu .")
            setAnimShow(false)
        }
    }

    fun setTokenEmpty(){
        token.value = ""
    }

    fun setAnimShow(boolean: Boolean) {
        isAnimationShow.value = boolean
    }
}