package com.example.tdd.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.usecases.GetDataByIdUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DetailScreenViewModel(repo: TDDRepository, id: Int) : ViewModel() {
    private val getDataByIdUseCase = GetDataByIdUseCase(repo)

    val data = getDataByIdUseCase(id).stateIn(viewModelScope, SharingStarted.Lazily, null)
}