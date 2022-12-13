package com.twofa.duosec.models.recovery_code

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecoveryCodeRequest(
    @Json(name = "companyHexCode")
    val companyHexCode: String,
    @Json(name = "generateRecoveryCode")
    val generateRecoveryCode: Boolean
)
