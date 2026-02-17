package com.example.tdd

import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTDDRepository : TDDRepository {

    val dataFlow = MutableStateFlow<List<Data>>(emptyList())
    val fetchDataFlow = MutableStateFlow<APIResult<Unit>>(APIResult.Success(Unit))
    private val dataByIdFlows = mutableMapOf<Int, MutableStateFlow<Data?>>()

    fun getOrCreateDataByIdFlow(id: Int): MutableStateFlow<Data?> {
        return dataByIdFlows.getOrPut(id) { MutableStateFlow(null) }
    }

    override fun fetchData(): Flow<APIResult<Unit>> = fetchDataFlow

    override fun getData(): Flow<List<Data>> = dataFlow

    override fun getDataById(id: Int): Flow<Data?> = getOrCreateDataByIdFlow(id)
}

