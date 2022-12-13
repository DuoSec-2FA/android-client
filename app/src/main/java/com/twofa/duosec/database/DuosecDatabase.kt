package com.twofa.duosec.database;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase
import com.twofa.duosec.models.jwt.JwtPayloadDatabase

@Database(
    entities = [
        JwtPayloadDatabase::class
    ],
    version = 1,
)
abstract class DuosecDatabase : RoomDatabase() {
    abstract val jwtPayloadDao: JwtPayloadDao;

    companion object {
        @Volatile
        private var INSTANCE: DuosecDatabase? = null

        fun getInstance(context: Context): DuosecDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DuosecDatabase::class.java,
                    "duosec_db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
