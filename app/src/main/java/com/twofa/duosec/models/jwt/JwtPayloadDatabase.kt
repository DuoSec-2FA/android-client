package com.twofa.duosec.models.jwt

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.twofa.duosec.utils.Utils

@Entity(
    tableName = "jwt_payloads",
    primaryKeys = ["employeeUniqueIdHex"]
)
data class JwtPayloadDatabase(
    val companyName: String,
    val employeeUniqueIdHex: String,
    val otpRefreshDuration: Int,
    val secret: String,
    val algorithm: String,
    val exp: Long,
    val iat: Long,
)
