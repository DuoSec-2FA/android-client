package com.twofa.duosec.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/get-qr-code")
    suspend fun getQrCode(@Query("companyEmployeeHash") companyEmployeeHash: String) : Response<String>
}