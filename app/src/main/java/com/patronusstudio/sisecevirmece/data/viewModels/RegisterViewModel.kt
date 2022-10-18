package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.GenderEnum
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.UserModelRegister
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {

    private val networkRepository by lazy { NetworkRepository() }
    private val localRepository by lazy { LocalRepository() }

    var emailError = MutableStateFlow(false)
        private set
    var userEmail = MutableStateFlow("")
        private set

    private val _selectedGender = MutableStateFlow(GenderEnum.NONE)
    val selectedGender: StateFlow<GenderEnum> get() = _selectedGender

    private val _isLockedPassword = MutableStateFlow(true)
    val isLockedPassword: StateFlow<Boolean> get() = _isLockedPassword

    private val _userPassword = MutableStateFlow("")
    val userPassword: StateFlow<String> get() = _userPassword

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _userToken = MutableStateFlow<String?>(null)
    val userToken : StateFlow<String?> get() = _userToken

    fun setUserEmail(mail: String) {
        userEmail.value = mail
    }

    fun setUserEmailError(boolean: Boolean) {
        emailError.value = boolean
    }

    fun setSelectedGender(genderEnum: GenderEnum) {
        _selectedGender.value = genderEnum
    }

    fun setIsLockedPassword(isLocked: Boolean) {
        _isLockedPassword.value = isLocked
    }

    fun setUserPassword(userPassword: String) {
        _userPassword.value = userPassword
    }

    fun setUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun register(mContext: Context) {
        CoroutineScope(Dispatchers.Main).launch finish@{
            _isLoading.value = true
            if (isEmailUsable(mContext).not()) {
                _isLoading.value = false
                return@finish
            }
            if (isUsernameUsable(mContext).not()) {
                _isLoading.value = false
                return@finish
            }
            val userModelRegister = UserModelRegister(
                username.value,
                userEmail.value,
                userPassword.value,
                selectedGender.value.enumType
            )
            val registerResult = withContext(Dispatchers.IO) {
                networkRepository.register(userModelRegister)
            }
            if (registerResult.body() == null || registerResult.body()!!.status != HttpStatusEnum.OK) {
                _isLoading.value = false
                _errorMessage.value =
                    registerResult.body()?.message
                        ?: mContext.getString(R.string.getting_some_error)
                return@finish
            }
            writeUserTokenOnLocalStorage(mContext,registerResult.body()!!.token!!)
            _isLoading.value = false
            _userToken.value = registerResult.body()!!.token
        }

    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private suspend fun isEmailUsable(mContext: Context): Boolean {
        val controlResponse = withContext(Dispatchers.IO) {
            networkRepository.emailControl(userEmail.value)
        }
        if (controlResponse.body() == null || controlResponse.body()!!.status != HttpStatusEnum.OK) {
            _errorMessage.value = controlResponse.body()?.message ?: mContext.getString(R.string.getting_some_error)
            return false
        }
        return true
    }

    private suspend fun isUsernameUsable(mContext: Context): Boolean {
        val controlResponse = withContext(Dispatchers.IO) {
            networkRepository.usernameControl(username.value)
        }
        if (controlResponse.body() == null || controlResponse.body()!!.status != HttpStatusEnum.OK) {
            _errorMessage.value = controlResponse.body()?.message ?: mContext.getString(R.string.getting_some_error)
            return false
        }
        return true
    }

    private suspend fun writeUserTokenOnLocalStorage(mContext: Context,token:String){
        withContext(Dispatchers.IO) {
            localRepository.setUserTokenOnLocal(mContext,token)
        }
    }
}
