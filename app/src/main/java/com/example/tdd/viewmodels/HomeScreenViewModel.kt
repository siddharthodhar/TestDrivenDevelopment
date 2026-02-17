package com.example.tdd.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import com.example.tdd.viewmodels.domain.usecases.FetchDataUseCase
import com.example.tdd.viewmodels.domain.usecases.GetDataUseCase
import kotlinx.coroutines.launch

class HomeScreenViewModel(repo: TDDRepository) : ViewModel() {

    private val fetchDataUseCase = FetchDataUseCase(repo)
    private val getDataUseCase = GetDataUseCase(repo)

    var data by mutableStateOf<List<Data>>(listOf())
        private set

    var fetchState by mutableStateOf<APIResult<Unit>>(APIResult.Success(Unit))
        private set

    init {
        viewModelScope.launch {
            getDataUseCase().collect { data = it }
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            fetchDataUseCase().collect { fetchState = it }
        }
    }

    fun clearFetchState() {
        fetchState = APIResult.Success(Unit)
    }
}