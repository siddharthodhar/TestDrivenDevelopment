package com.example.tdd.model.data.remote.datasource

import com.example.tdd.model.ApiResponse
import com.example.tdd.model.data.remote.entity.DataResponse
import com.example.tdd.model.data.remote.service.TDDApiService
import kotlinx.coroutines.CancellationException
import retrofit2.Response

class TDDAPIDataSource(
    private val api: TDDApiService
) {

    suspend fun fetch(): ApiResponse<List<DataResponse>> {
        return safeAPICall {
            api.fetch()
        }
    }

    private suspend fun <T> safeAPICall(block: suspend () -> Response<T>): ApiResponse<T> {
        return try {
                val r = block()
                if (r.isSuccessful) {
                    if (r.body() != null) {
                        ApiResponse.Success(r.code(), r.body() as T)
                    } else {
                        ApiResponse.Error(r.code(), IllegalStateException("Empty Body"))
                    }
                } else {
                    ApiResponse.Error(r.code(), IllegalStateException(r.errorBody()?.string().orEmpty()))
                }
        } catch (e: CancellationException) {
            throw e
        } catch (t: Throwable) {
            return ApiResponse.Error(-1, t)
        }
    }
}