package com.ferhat.comprc.data.remote

import com.ferhat.comprc.domain.model.PowerCommandRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PowerApiService {
    @POST("/command")
    suspend fun sendCommand(@Body req: PowerCommandRequest): Response<ResponseBody>

    @GET("/")
    suspend fun checkServer(): Response<ResponseBody>
}

