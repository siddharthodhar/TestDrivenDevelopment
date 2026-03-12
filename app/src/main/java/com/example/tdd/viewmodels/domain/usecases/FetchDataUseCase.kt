package com.example.tdd.viewmodels.domain.usecases

import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchDataUseCase @Inject constructor(private val repository: TDDRepository) {

    operator fun invoke(): Flow<APIResult<Unit>> {
        return repository.fetchData()
    }
}