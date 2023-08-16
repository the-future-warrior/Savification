package com.mr_17.savification.room

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface TestingDao {
    @Upsert
    suspend fun upsertTest(test: Test)
}