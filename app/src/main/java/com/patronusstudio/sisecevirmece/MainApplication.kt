package com.patronusstudio.sisecevirmece

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication :Application(){

    companion object{
        var authToken:String = ""
    }
}