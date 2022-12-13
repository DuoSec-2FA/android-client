package com.twofa.duosec.api

import com.twofa.duosec.models.recovery_code.RecoveryCodeRequest
import com.twofa.duosec.models.recovery_code.RecoveryCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceDao {
    /*
        Request Body
        {
          "companyHexCode": "string",
          "generateRecoveryCode": true
        }
     */
    @POST("/get-recovery-code")
    suspend fun getRecoveryCode(@Body recoveryCode: RecoveryCodeRequest): Response<RecoveryCodeResponse>
}