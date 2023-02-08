package com.patronusstudio.sisecevirmece2.data.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel:ViewModel()  {

    val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    open fun setLoadingStatus(status:Boolean){
        _isLoading.value = status
    }
}