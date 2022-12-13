package com.twofa.duosec.models.recovery_code

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecoveryCodeResponse(
    @Json(name = "recoveryCode")
    val recoveryCode: String
)
