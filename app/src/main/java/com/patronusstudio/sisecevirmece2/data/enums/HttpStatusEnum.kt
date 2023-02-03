package com.patronusstudio.sisecevirmece2.data.enums

import com.google.gson.annotations.SerializedName


enum class HttpStatusEnum(val code: String) {
    @SerializedName("OK")
    OK("OK"),
    @SerializedName("BadRequest")
    BadRequest("BadRequest"),
    @SerializedName("Unauthorized")
    Unauthorized("Unauthorized"),
    @SerializedName("NotFound")
    NotFound("NotFound"),
    @SerializedName("NOT_ACCEPTABLE")
    NotAcceptable("NOT_ACCEPTABLE"),
    @SerializedName("Unknown")
    Unknown("Unknown");


}