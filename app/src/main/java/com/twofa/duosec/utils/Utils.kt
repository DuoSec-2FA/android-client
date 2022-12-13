package com.twofa.duosec.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPayload(jwtToken: String): String {
        /*
            Splitting jwt tokens in 3 parts
            1. Header
            2. Payload
            3. Signature
         */
        val chunks = jwtToken.split('.')
        val (headerChunk, payloadChunk, signatureChunk) = chunks

        // Using Base64 decoder to decouple the payload portion of jwt token
        val decoder = Base64.getUrlDecoder()

        // Got payload in JSON String from payloadChunk in token
        val payload: String = String(decoder.decode(payloadChunk))
        return payload
    }

    fun generateOTP(): Int {
        val r = Random()
        val low = 1000000000
        val high = 9999999999
        return r.nextInt((high - low).toInt()) + low
    }
}