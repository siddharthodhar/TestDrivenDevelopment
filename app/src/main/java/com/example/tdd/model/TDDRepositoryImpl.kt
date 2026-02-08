package com.example.tdd.model

import com.example.tdd.model.data.local.datasource.TDDLocalDataSource
import com.example.tdd.model.data.local.entity.toDomain
import com.example.tdd.model.data.remote.datasource.TDDAPIDataSource
import com.example.tdd.model.data.remote.entity.DataResponse
import com.example.tdd.model.data.remote.entity.toDataEntity
import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TDDRepositoryImpl(
    private val remote: TDDAPIDataSource,
    private val local: TDDLocalDataSource
): TDDRepository {
    override fun fetchData(): Flow<APIResult<Unit>> = flow {
        emit(APIResult.loading())
        when(val res = remote.fetch()) {
            is ApiResponse.Error -> {
                emit(APIResult.failure(res.throwable))
            }
            is ApiResponse.Success<List<DataResponse>> -> {
                local.insertData(
                    res.data.map {
                        it.toDataEntity()
                    }
                )
                emit(APIResult.success(Unit))
            }
        }
    }

    override fun getData(): Flow<List<Data>> = local.getData().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getDataById(id: Int): Flow<Data?> = local.getDataById(id).map {
        it?.toDomain()
    }
}