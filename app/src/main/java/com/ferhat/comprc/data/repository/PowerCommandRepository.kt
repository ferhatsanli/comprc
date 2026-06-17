package com.ferhat.comprc.data.repository

import android.util.Log
import com.ferhat.comprc.data.remote.PowerApiService
import com.ferhat.comprc.domain.model.PowerCommandRequest
import com.ferhat.comprc.domain.model.ServerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PowerCommandRepository(private val api: PowerApiService) {
    suspend fun sendShutdownCommand(): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.sendCommand(PowerCommandRequest(command = "shutdown"))
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "")
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkServerStatus(): ServerStatus = withContext(Dispatchers.IO) {
        val TAG = "ferhat"
        Log.i(TAG, "checkServerStatus: basladi")
        var response: Response<ResponseBody>
        var body = ""
        return@withContext try {
            response = api.checkServer()
            body = response.body()?.string() ?: ""
            Log.i(TAG, "checkServerStatus:  body=$body")

            if (response.isSuccessful) {
                if (body.trim() == "Hello, World!") {
                    ServerStatus.OK
                } else {
                    ServerStatus.BAD
                }
            } else {
                ServerStatus.BAD
            }
        } catch (e: IOException) {
            Log.i(TAG, "checkServerStatus: IOException body=$body")
            Log.e(TAG, "checkServerStatus: ${e.message}", )
            ServerStatus.BAD
        } catch (e: HttpException) {
            Log.i(TAG, "checkServerStatus: HttpException body=$body")
            Log.e(TAG, "checkServerStatus: ${e.message}", )
            ServerStatus.BAD
        }
    }
}

