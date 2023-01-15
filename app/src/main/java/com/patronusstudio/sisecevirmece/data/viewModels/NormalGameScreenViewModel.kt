package com.patronusstudio.sisecevirmece.data.viewModels

import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.data.enums.BottleTouchListener
import com.patronusstudio.sisecevirmece.data.enums.TruthDareEnum
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NormalGameScreenViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _truthDareSelected = MutableStateFlow(TruthDareEnum.NOT_SELECTED)
    val truthDareSelected: StateFlow<TruthDareEnum> get() = _truthDareSelected

    private val _bottleTouchListener = MutableStateFlow(BottleTouchListener.INIT)
    val bottleTouchListener: StateFlow<BottleTouchListener> get() = _bottleTouchListener

    fun setTruthDareSelected(truthDareEnum: TruthDareEnum) {
        _truthDareSelected.value = truthDareEnum
    }

    fun setBottleTouchListener(bottleTouchListener: BottleTouchListener) {
        _bottleTouchListener.value = bottleTouchListener
    }
}