package com.twofa.duosec.models.jwt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JwtPayloadJson(
    @Json(name = "companyName")
    val companyName: String,
    @Json(name = "employeeUniqueIdHex")
    val employeeUniqueIdHex: String,
    @Json(name = "otpRefreshDuration")
    val otpRefreshDuration: Int,
    @Json(name = "secret")
    val secret: ByteArray,
    @Json(name = "algorithm")
    val algorithm: String,
    @Json(name = "exp")
    val exp: Long,
    @Json(name = "iat")
    val iat: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JwtPayloadJson

        if (companyName != other.companyName) return false
        if (employeeUniqueIdHex != other.employeeUniqueIdHex) return false
        if (otpRefreshDuration != other.otpRefreshDuration) return false
        if (!secret.contentEquals(other.secret)) return false
        if (algorithm != other.algorithm) return false
        if (exp != other.exp) return false
        if (iat != other.iat) return false

        return true
    }

    override fun hashCode(): Int {
        var result = companyName.hashCode()
        result = 31 * result + employeeUniqueIdHex.hashCode()
        result = 31 * result + otpRefreshDuration
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + algorithm.hashCode()
        result = 31 * result + exp.hashCode()
        result = 31 * result + iat.hashCode()
        return result
    }
}