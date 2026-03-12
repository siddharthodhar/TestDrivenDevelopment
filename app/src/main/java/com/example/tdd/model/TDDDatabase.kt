package com.example.tdd.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tdd.model.data.local.dao.TDDDao
import com.example.tdd.model.data.local.entity.DataEntity

@Database(
    entities = [DataEntity::class],
    version = 1
)
abstract class TDDDatabase: RoomDatabase() {
    abstract fun tddDao(): TDDDao
}

