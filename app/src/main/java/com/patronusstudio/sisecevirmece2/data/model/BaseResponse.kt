package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

abstract class BaseResponse {
    abstract val message: String?
    abstract val status: HttpStatusEnum

}