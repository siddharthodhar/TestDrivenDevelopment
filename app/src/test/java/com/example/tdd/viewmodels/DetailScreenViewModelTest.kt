package com.example.tdd.viewmodels

import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DetailScreenViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = object : TestWatcher() {
        override fun starting(description: Description?) {
            Dispatchers.setMain(StandardTestDispatcher())
        }

        override fun finished(description: Description?) {
            Dispatchers.resetMain()
        }
    }
    private val repo = mock<TDDRepository>()
    private lateinit var viewModel: DetailScreenViewModel
    private val mockData = Data(
        1,
        1,
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun data_first_emits_null_then_returns_data_for_given_id() = runTest {
        whenever(repo.getDataById(1))
            .thenReturn(flowOf(mockData))

        viewModel = DetailScreenViewModel(repo, 1)

        val emissions = mutableListOf<Data?>()
        val job = launch {
            viewModel.data.take(2).collect {
                emissions.add(it)
            }
        }

        advanceUntilIdle()
        job.join()

        assertEquals(2, emissions.size)
        assertNull(emissions.first())
        assertEquals(mockData, emissions.last())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun data_emits_null_when_id_not_found() = runTest {
        whenever(repo.getDataById(999))
            .thenReturn(flowOf(null))

        viewModel = DetailScreenViewModel(repo, 999)

        advanceUntilIdle()

        assertNull(viewModel.data.value)
    }
}