package com.patronusstudio.sisecevirmece2.data.model

import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum

class SampleResponse(override val message: String?, override val status: HttpStatusEnum) :
    BaseResponse()