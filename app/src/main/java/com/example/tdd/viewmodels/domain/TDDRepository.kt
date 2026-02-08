package com.example.tdd.viewmodels.domain

import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.flow.Flow

interface TDDRepository {

    fun fetchData(): Flow<APIResult<Unit>>

    fun getData(): Flow<List<Data>>

    fun getDataById(id: Int): Flow<Data?>
}