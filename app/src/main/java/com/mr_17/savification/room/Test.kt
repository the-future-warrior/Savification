package com.mr_17.savification.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Test(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val data: String
)
