package com.patronusstudio.sisecevirmece2.data.utils

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

fun MixpanelAPI.singleEventSend(eventName:String){
    val props = JSONObject()
    this.track(eventName, props)
}

fun MixpanelAPI.multiEventSend(eventName:String,events:Map<String,String>){
    val props = JSONObject()
    events.keys.forEach { key ->
        events[key]?.let { data ->
            props.put(key, data)
        }
    }
    this.track(eventName, props)
}