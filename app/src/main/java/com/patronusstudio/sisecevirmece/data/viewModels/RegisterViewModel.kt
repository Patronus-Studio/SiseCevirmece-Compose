package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.model.UserModelRegister
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import com.patronusstudio.sisecevirmece.ui.screens.GenderEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : ViewModel() {

    private val networkRepository by lazy { NetworkRepository() }

    var emailError = MutableStateFlow(false)
        private set
    var userEmail = MutableStateFlow("me.iamcodder2@gmail.com")
        private set

    private val _selectedGender = MutableStateFlow(GenderEnum.MALE)
    val selectedGender: StateFlow<GenderEnum> get() = _selectedGender

    private val _isLockedPassword = MutableStateFlow(true)
    val isLockedPassword: StateFlow<Boolean> get() = _isLockedPassword

    private val _userPassword = MutableStateFlow("123123")
    val userPassword: StateFlow<String> get() = _userPassword

    private val _username = MutableStateFlow("iamcodder2")
    val username: StateFlow<String> get() = _username

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

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
            val userModelRegister = UserModelRegister(
                username.value,
                userPassword.value,
                userEmail.value,
                selectedGender.value.enumType
            )
            val result = withContext(Dispatchers.IO) {
                networkRepository.register(userModelRegister)
            }
            if (result.body() == null || result.body()!!.status != HttpStatusEnum.OK) {
                _isLoading.value = false
                _errorMessage.value =
                    result.body()?.message ?: mContext.getString(R.string.getting_some_error)
                return@finish
            }
            _isLoading.value = false
        }

    }

    fun clearErrorMessage(){
        _errorMessage.value = null
    }
}
