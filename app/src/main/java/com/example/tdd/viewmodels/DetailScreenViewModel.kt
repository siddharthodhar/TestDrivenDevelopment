package com.example.tdd.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import com.example.tdd.viewmodels.domain.usecases.GetDataByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailScreenViewModel(repo: TDDRepository, id: Int) : ViewModel() {
    private val getDataByIdUseCase = GetDataByIdUseCase(repo)

    var data by mutableStateOf<Data?>(null)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDataByIdUseCase(id).collect { data = it }
        }
    }
}