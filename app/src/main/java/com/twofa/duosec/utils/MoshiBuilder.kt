package com.twofa.duosec.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.twofa.duosec.models.jwt.JwtPayloadJson
import java.lang.reflect.Type

object MoshiBuilder {
    // Moshi Instance
    private val moshi = Moshi.Builder().build()

    // Moshi Type for Type: List<JwtPayloads>
    private val type: Type = Types.newParameterizedType(
        List::class.java,
        JwtPayloadJson::class.java
    )

    // JwtPayloadAdapter which works on JwtPayload Data Class
    private val jwtPayloadJsonAdapter: JsonAdapter<JwtPayloadJson> = moshi.adapter(JwtPayloadJson::class.java)

    // JwtPayloadListAdapter which works on list<JwtPayload>
    private val jwtPayloadJsonListAdapter: JsonAdapter<List<JwtPayloadJson>> = moshi.adapter<List<JwtPayloadJson>>(type)

    fun getJwtPayloadAdapter(): JsonAdapter<JwtPayloadJson> {
        return jwtPayloadJsonAdapter;
    }

    fun getJwtPayloadListAdapter(): JsonAdapter<List<JwtPayloadJson>> {
        return jwtPayloadJsonListAdapter;
    }
}