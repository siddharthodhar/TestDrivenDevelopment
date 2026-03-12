package com.example.tdd.viewmodels.domain.usecases

import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDataUseCase @Inject constructor(private val repository: TDDRepository) {

    operator fun invoke(): Flow<List<Data>> {
        return repository.getData()
    }
}