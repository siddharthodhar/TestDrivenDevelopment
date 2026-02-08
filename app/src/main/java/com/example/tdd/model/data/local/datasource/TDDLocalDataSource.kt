package com.example.tdd.model.data.local.datasource

import com.example.tdd.model.data.local.dao.TDDDao
import com.example.tdd.model.data.local.entity.DataEntity
import kotlinx.coroutines.flow.Flow

class TDDLocalDataSource(
    private val dao: TDDDao
) {

    suspend fun insertData(dataEntities: List<DataEntity>) {
        dao.insertData(dataEntities)
    }

    fun getData(): Flow<List<DataEntity>> {
        return dao.getData()
    }

    fun getDataById(id: Int): Flow<DataEntity?> {
        return dao.getDataById(id)
    }
}