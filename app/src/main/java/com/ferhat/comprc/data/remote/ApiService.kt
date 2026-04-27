package com.ferhat.comprc.data.remote

import com.ferhat.comprc.data.model.CommandRequest
import com.ferhat.comprc.data.model.CommandResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("execute")
    suspend fun executeCommand(@Body request: CommandRequest): CommandResponse
}