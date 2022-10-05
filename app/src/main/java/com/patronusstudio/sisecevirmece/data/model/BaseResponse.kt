package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

abstract class BaseResponse {
    abstract val message: String?
    abstract val status: HttpStatusEnum

}