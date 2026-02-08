package com.example.tdd.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tdd.model.data.local.entity.DataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TDDDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(dataEntities: List<DataEntity>)

    @Query("SELECT * FROM users")
    fun getData(): Flow<List<DataEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getDataById(id: Int): Flow<DataEntity?>
}