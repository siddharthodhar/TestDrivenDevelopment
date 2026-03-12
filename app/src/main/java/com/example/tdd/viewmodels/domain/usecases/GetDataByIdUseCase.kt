package com.example.tdd.viewmodels.domain.usecases

import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDataByIdUseCase @Inject constructor(private val repository: TDDRepository) {

    operator fun invoke(id: Int): Flow<Data?> {
        return repository.getDataById(id)
    }
}