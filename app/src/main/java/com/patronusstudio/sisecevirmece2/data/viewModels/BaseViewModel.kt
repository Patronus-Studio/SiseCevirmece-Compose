package com.patronusstudio.sisecevirmece2.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece2.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

open class BaseViewModel:ViewModel()  {

    val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    open fun setLoadingStatus(status:Boolean){
        _isLoading.value = status
    }
}