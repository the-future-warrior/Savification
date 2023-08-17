package com.mr_17.savification.room

import android.graphics.drawable.Icon
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Test(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val packageName: String?,
    val title: String?,
    val text: String?,
    val subText: String?,
    val contentText: String?,
    val smallIcon: Int?,
    val largeIcon: Int?,
    val timeStamp: Long?
)
