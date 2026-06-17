package com.ferhat.comprc.data.remote

import com.ferhat.comprc.data.model.TimeResponse
import retrofit2.http.GET

interface TimeApiService {
    @GET("/time")
    suspend fun getTime(): TimeResponse
}