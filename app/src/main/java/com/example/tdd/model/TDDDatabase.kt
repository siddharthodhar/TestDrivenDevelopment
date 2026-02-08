package com.example.tdd.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tdd.model.data.local.dao.TDDDao
import com.example.tdd.model.data.local.entity.DataEntity

object RoomDatabaseBuilder {

    lateinit var database: TDDDatabase

    fun createDatabase(context: Context) {
        database = Room.databaseBuilder(
            context, TDDDatabase::class.java, "tdd_database"
        ).build()
    }

    fun getTDDDao() = database.tddDao()
}

@Database(
    entities = [DataEntity::class],
    version = 1
)
abstract class TDDDatabase: RoomDatabase() {
    abstract fun tddDao(): TDDDao
}

