package com.example.tdd.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import com.example.tdd.viewmodels.domain.usecases.GetDataByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getDataByIdUseCase: GetDataByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    constructor(repo: TDDRepository, id: Int) : this(
        getDataByIdUseCase = GetDataByIdUseCase(repo),
        savedStateHandle = SavedStateHandle(mapOf(ID_KEY to id))
    )

    private val id = checkNotNull(savedStateHandle.get<Int>(ID_KEY))

    var data by mutableStateOf<Data?>(null)
        private set

    init {
        viewModelScope.launch {
            getDataByIdUseCase(id).collect { data = it }
        }
    }

    companion object {
        private const val ID_KEY = "id"
    }
}