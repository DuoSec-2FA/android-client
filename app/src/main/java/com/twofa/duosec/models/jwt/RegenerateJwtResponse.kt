package com.twofa.duosec.models.jwt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegenerateJwtResponse(
    @Json(name = "jwtToken")
    val jwtToken: String
)