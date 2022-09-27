package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel: ViewModel() {

    var emailError = MutableStateFlow(false)
        private set
    var userEmail = MutableStateFlow("")
        private set

    fun setUserEmail(mail: String) {
        userEmail.value = mail
    }

    fun setUserEmailError(boolean: Boolean) {
        emailError.value = boolean
    }
}