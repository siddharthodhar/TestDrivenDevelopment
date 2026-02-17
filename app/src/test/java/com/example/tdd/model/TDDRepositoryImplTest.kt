package com.example.tdd.model

import com.example.tdd.model.data.local.datasource.TDDLocalDataSource
import com.example.tdd.model.data.local.entity.DataEntity
import com.example.tdd.model.data.local.entity.toDomain
import com.example.tdd.model.data.remote.datasource.TDDAPIDataSource
import com.example.tdd.model.data.remote.entity.DataResponse
import com.example.tdd.model.data.remote.entity.toDataEntity
import com.example.tdd.viewmodels.domain.APIResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TDDRepositoryImplTest {

    private val remote = mock<TDDAPIDataSource>()
    private val local = mock<TDDLocalDataSource>()
    private val repo = TDDRepositoryImpl(remote, local)
    private val mockResponse = DataResponse(
        1,
        1,
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
    )

    val mockEntity = DataEntity(
        1,
        1,
        "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
        "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
    )

    @Test
    fun fetch_data_emits_loading_then_success_when_api_succeeds() = runTest {
        val apiResponse = ApiResponse.Success(
            200, listOf(mockResponse)
        )

        whenever(remote.fetch()).thenReturn(apiResponse)

        val emissions = repo.fetchData().toList()

        assertTrue(emissions.first() is APIResult.Loading)
        assertTrue(emissions[1] is APIResult.Success)
        verify(local).insertData(any())
    }

    @Test
    fun fetch_data_emits_loading_then_failure_when_api_fails() = runTest {
        val apiResponse = ApiResponse.Error(
            400, IOException("Bad request")
        )

        whenever(remote.fetch()).thenReturn(apiResponse)

        val emissions = repo.fetchData().toList()

        assertTrue(emissions.first() is APIResult.Loading)
        assertTrue(emissions[1] is APIResult.Error)
        verify(local, never()).insertData(any())
    }

    @Test
    fun fetch_data_inserts_correct_data_to_local_database() = runTest {
        val mockResponseList = listOf(
            mockResponse, mockResponse.copy(userId = 2, id = 2)
        )
        val apiResponse = ApiResponse.Success(
            200, mockResponseList
        )

        whenever(remote.fetch()).thenReturn(apiResponse)

        repo.fetchData().toList()

        verify(local).insertData(eq(mockResponseList.map { it.toDataEntity() }))
    }

    @Test
    fun get_data_will_fetch_and_provide_list_from_local_database() = runTest {
        val dbResult = flowOf(listOf(
                mockEntity, mockEntity.copy(userId = 2, id = 2)
            )
        )

        whenever(local.getData()).thenReturn(dbResult)

        val emission = repo.getData().toList()

        assertEquals(2, emission.first().size)
        assertEquals(1, emission.first().first().id)
    }

    @Test
    fun get_data_by_will_fetch_and_provide_single_data_matching_id_from_local_database() = runTest {
        val dbResult = flowOf(mockEntity)

        whenever(local.getDataById(1)).thenReturn(dbResult)

        val emission = repo.getDataById(1).toList()

        assertEquals(mockEntity.toDomain(), emission.first())
    }

    @Test
    fun get_data_by_will_fetch_and_provide_null_if_matching_id_not_found_from_local_database() = runTest {
        whenever(local.getDataById(1)).thenReturn(flowOf(null))

        val emission = repo.getDataById(1).toList()

        assertNull(emission.first())
    }
}