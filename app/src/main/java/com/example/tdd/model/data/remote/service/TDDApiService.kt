package com.example.tdd.model.data.remote.service

import com.example.tdd.model.data.remote.entity.DataResponse
import retrofit2.Response
import retrofit2.http.GET

interface TDDApiService {

    @GET("/posts")
    suspend fun fetch(): Response<List<DataResponse>>
}