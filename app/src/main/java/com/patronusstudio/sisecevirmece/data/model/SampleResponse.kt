package com.patronusstudio.sisecevirmece.data.model

import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum

class SampleResponse(override val message: String?, override val status: HttpStatusEnum) :
    BaseResponse()