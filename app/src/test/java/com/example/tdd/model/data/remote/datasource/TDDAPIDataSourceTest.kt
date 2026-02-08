package com.example.tdd.model.data.remote.datasource

import com.example.tdd.model.ApiResponse
import com.example.tdd.model.data.remote.service.TDDApiService
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TDDAPIDataSourceTest {
    private lateinit var server: MockWebServer
    private lateinit var api: TDDApiService
    private val responseJson = "[\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
            "    \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"title\": \"qui est esse\",\n" +
            "    \"body\": \"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 3,\n" +
            "    \"title\": \"ea molestias quasi exercitationem repellat qui ipsa sit aut\",\n" +
            "    \"body\": \"et iusto sed quo iure\\nvoluptatem occaecati omnis eligendi aut ad\\nvoluptatem doloribus vel accusantium quis pariatur\\nmolestiae porro eius odio et labore et velit aut\"\n" +
            "  }]"

    @Before
    fun setUp() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(TDDApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun fetch_api_success() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )
        val response = api.fetch()

        assertTrue(response.isSuccessful)
        assertEquals(1, response.body()?.first()?.userId)
    }

    @Test
    fun fetch_api_failed() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
        )
        val response = api.fetch()

        assertFalse(response.isSuccessful)
        assertNull(response.body()?.first()?.userId)
    }

    @Test
    fun fetch_datasource_success() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseJson)
        )
        val dataSource = TDDAPIDataSource(api)
        val apiResponse = dataSource.fetch()

        assertTrue(apiResponse is ApiResponse.Success)

        val successResponse = apiResponse as ApiResponse.Success
        assertEquals(200, successResponse.code)
        assertEquals(1, successResponse.data.first().userId)
    }

    @Test
    fun fetch_datasource_success_with_no_body() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("null")
        )
        val dataSource = TDDAPIDataSource(api)
        val apiResponse = dataSource.fetch()

        assertTrue(apiResponse is ApiResponse.Error)

        val errorResponse = apiResponse as ApiResponse.Error
        assertEquals(200, errorResponse.code)
        assertTrue(errorResponse.throwable is IllegalStateException)
        assertEquals("Empty Body", (errorResponse.throwable as IllegalStateException).message)
    }

    @Test
    fun fetch_failed_with_no_body() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(400)
        )
        val dataSource = TDDAPIDataSource(api)
        val apiResponse = dataSource.fetch()

        assertTrue(apiResponse is ApiResponse.Error)

        val errorResponse = apiResponse as ApiResponse.Error
        assertEquals(400, errorResponse.code)
        assertTrue(errorResponse.throwable is IllegalStateException)
    }

    @Test
    fun fetch_failed_with_network_exception() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{ invalid json")
        )
        val dataSource = TDDAPIDataSource(api)
        val apiResponse = dataSource.fetch()

        assertTrue(apiResponse is ApiResponse.Error)

        val errorResponse = apiResponse as ApiResponse.Error
        assertEquals(-1, errorResponse.code)
        assertTrue(errorResponse.throwable is JsonDataException)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetch_when_cancelled_throws_cancellation_exception() = runTest {
        server.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )
        val dataSource = TDDAPIDataSource(api)

        val deferred = async {
            dataSource.fetch()
        }

        advanceTimeBy(1000)
        deferred.cancel()

        try {
            deferred.await()
            fail("Cancellation Exception not thrown")
        } catch (e: CancellationException) {
            assertNotNull(e)
        }
    }

}