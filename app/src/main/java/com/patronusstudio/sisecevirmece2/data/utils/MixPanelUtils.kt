package com.patronusstudio.sisecevirmece2.data.utils

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject




fun MixpanelAPI.singleEventSend(eventName:String){
    val props = JSONObject()
    this.track(eventName, props)
}