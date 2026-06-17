package com.ferhat.comprc.data.repository

import android.util.Log
import com.ferhat.comprc.data.model.CommandRequest
import com.ferhat.comprc.data.model.CommandResponse
import com.ferhat.comprc.data.remote.ApiService

class CommandRepository(private val apiService: ApiService) {

    suspend fun executeCommand(command: String): Result<CommandResponse> {
        return try {
            val response = apiService.executeCommand(CommandRequest(command))
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ferhat", "executeCommand: ${e.message}", )
            Result.failure(e)
        }
    }
}