package com.patronusstudio.sisecevirmece.data.enums

import com.google.gson.annotations.SerializedName


enum class HttpStatusEnum(val code: String) {
    @SerializedName("OK")
    OK("200"),
    @SerializedName("BadRequest")
    BadRequest("400"),
    @SerializedName("Unauthorized")
    Unauthorized("401"),
    @SerializedName("NotFound")
    NotFound("404"),
    @SerializedName("NOT_ACCEPTABLE")
    NotAcceptable("406"),
    @SerializedName("Unknown")
    Unknown("0");


}