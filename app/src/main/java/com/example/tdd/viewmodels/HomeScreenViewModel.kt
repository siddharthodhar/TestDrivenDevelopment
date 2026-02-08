package com.example.tdd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.usecases.FetchDataUseCase
import com.example.tdd.viewmodels.domain.usecases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(repo: TDDRepository) : ViewModel() {

    private val fetchDataUseCase = FetchDataUseCase(repo)
    private val getDataUseCase = GetDataUseCase(repo)

    val data = getDataUseCase().stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    fun fetchData(): Flow<APIResult<Unit>> = fetchDataUseCase()
}