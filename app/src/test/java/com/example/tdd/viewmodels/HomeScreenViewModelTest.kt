package com.example.tdd.viewmodels

import com.example.tdd.viewmodels.domain.APIResult
import com.example.tdd.viewmodels.domain.TDDRepository
import com.example.tdd.viewmodels.domain.entity.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class HomeScreenViewModelTest {
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
    private lateinit var viewModel: HomeScreenViewModel
    val mockData = Data(
        1,
        1,
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun get_data_initially_returns_empty_list() = runTest {
        whenever(repo.getData())
            .thenReturn(flowOf(listOf(mockData)))

        viewModel = HomeScreenViewModel(repo)

        assertEquals(listOf<Data>(), viewModel.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun get_data_returns_data_once_available() = runTest {
        whenever(repo.getData())
            .thenReturn(flowOf(listOf(mockData)))

        viewModel = HomeScreenViewModel(repo)
        advanceUntilIdle()

        assertEquals(listOf(mockData), viewModel.data)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetch_data_calls_repo_fetch_data_and_inserts_data_into_db() = runTest {
        whenever(repo.getData())
            .thenReturn(flowOf(listOf()))
        whenever(repo.fetchData())
            .thenReturn(flowOf(APIResult.Loading, APIResult.Success(Unit)))

        viewModel = HomeScreenViewModel(repo)

        viewModel.fetchData()
        advanceUntilIdle()

        verify(repo).fetchData()
        assertTrue(viewModel.fetchState is APIResult.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetch_data_emits_error_on_failure() = runTest {
        val exception = RuntimeException("Network error")
        whenever(repo.getData())
            .thenReturn(flowOf(listOf()))
        whenever(repo.fetchData())
            .thenReturn(flowOf(APIResult.Error(exception)))

        viewModel = HomeScreenViewModel(repo)

        viewModel.fetchData()
        advanceUntilIdle()

        assertTrue(viewModel.fetchState is APIResult.Error)
        assertEquals(exception, (viewModel.fetchState as APIResult.Error).throwable)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clear_fetch_state_sets_fetch_state_to_success() = runTest {
        val exception = RuntimeException("Network error")
        whenever(repo.getData())
            .thenReturn(flowOf(listOf()))
        whenever(repo.fetchData())
            .thenReturn(flowOf(APIResult.Error(exception)))

        viewModel = HomeScreenViewModel(repo)
        viewModel.fetchData()
        viewModel.clearFetchState()
        advanceUntilIdle()

        assertTrue(viewModel.fetchState is APIResult.Success)
    }
}