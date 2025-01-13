package com.example.project

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        DiaryRecord::class,
    ],
    version = 3,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryRecordDao(): DiaryRecordDao
}
