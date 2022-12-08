package com.twofa.duosec.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JwtPayload(
    @Json(name = "algorithm")
    val algorithm: String,
    @Json(name = "exp")
    val exp: Long,
    @Json(name = "iat")
    val iat: Long,
    @Json(name = "otpRefreshDuration")
    val otpRefreshDuration: Int,
    @Json(name = "secret")
    val secret: List<Int>,
    @Json(name = "companyEmployeeHash")
    val companyEmployeeHash: String = "1234"
)