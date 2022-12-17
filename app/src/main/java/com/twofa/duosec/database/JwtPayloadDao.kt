package com.twofa.duosec.database

import androidx.room.*
import com.twofa.duosec.models.jwt.JwtPayloadDatabase

@Dao
interface JwtPayloadDao {
    @Query("SELECT * FROM jwt_payloads")
    suspend fun getAllJwtPayloads(): List<JwtPayloadDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJwtPayload(jwtPayload: JwtPayloadDatabase)

    @Update
    suspend fun updateJwtPayload(jwtPayload: JwtPayloadDatabase)

//    @Delete
//    suspend fun delete(jwtPayload: JwtPayloadDatabase)
//
//    @Delete
//    suspend fun deleteAll()
}