package com.twofa.duosec.api

import com.twofa.duosec.models.jwt.RegenerateJwtResponse
import com.twofa.duosec.models.recovery_code.RecoveryCodeRequest
import com.twofa.duosec.models.recovery_code.RecoveryCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface ApiServiceDao {
    @POST("/get-recovery-code")
    suspend fun getRecoveryCode(@Body recoveryCode: RecoveryCodeRequest): Response<RecoveryCodeResponse>

    @POST("/regenerate-jwt-token")
    suspend fun regenerateJwtToken(@Body companyHex: String): Response<RegenerateJwtResponse>
}