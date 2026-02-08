package com.example.tdd.viewmodels.domain.usecases

import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.flow.Flow

class GetDataUseCase(private val repository: TDDRepository) {

    operator fun invoke(): Flow<List<Data>> {
        return repository.getData()
    }
}