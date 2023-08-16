package com.mr_17.savification.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Test::class],
    version = 1
)
abstract class TestingDatabase : RoomDatabase() {
    abstract val testingDao: TestingDao

    companion object {

        private var INSTANCE: TestingDatabase? = null

        fun getInstance(context: Context): TestingDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, TestingDatabase::class.java, "testing.db").build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}