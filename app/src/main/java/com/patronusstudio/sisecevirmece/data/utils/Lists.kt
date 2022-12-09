package com.patronusstudio.sisecevirmece.data.utils

import com.patronusstudio.sisecevirmece.ui.screens.BaseModelWithIndex

fun <T : BaseModelWithIndex> T.removeModelOnList(list: List<T>): List<T> {
    val newList: MutableList<T> = list.toMutableList()
    return newList.filter {
        it.id != this.id
    }.map {
        if (it.id > this.id) {
            it.id = it.id - 1
            it
        } else it
    }
}