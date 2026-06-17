package com.ferhat.comprc.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhat.comprc.data.remote.PowerApiService
import com.ferhat.comprc.data.remote.TimeApiService
import com.ferhat.comprc.data.repository.PowerCommandRepository
import com.ferhat.comprc.data.repository.TimeRepository
import com.ferhat.comprc.domain.model.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("http://rpi.local:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
//        .create(PowerApiService::class.java)
    private val powerRepository = PowerCommandRepository(api.create(PowerApiService::class.java))

    private val timeRepository = TimeRepository(api.create(TimeApiService::class.java))
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onPowerButtonClick() {
        val TAG = "ferhat"

        Log.i(TAG, "onPowerButtonClick: basladi")
        Log.i(TAG, "onPowerButtonClick: target IP=${_uiState.value.serverIP}")
        _uiState.value = _uiState.value.copy(isPowerButtonLoading = true)
        Log.i(TAG, "onPowerButtonClick: ${_uiState.value.isPowerButtonLoading.toString()}")
        viewModelScope.launch {
            val result = powerRepository.sendShutdownCommand()
            _uiState.value = _uiState.value.copy(isPowerButtonLoading = false)
            // İstersen burada başarı/hata mesajı state'e eklenebilir
        }
    }

    fun onRefreshServerStatus() {
        val TAG = "ferhat"
        Log.i(TAG, "onRefreshServerStatus: refresh basladi")
        Log.i(TAG, "onPowerButtonClick: target IP=${_uiState.value.serverIP}")

        _uiState.value = _uiState.value.copy(isRefreshing = true)
        viewModelScope.launch {
            val status = powerRepository.checkServerStatus()
            Log.i(TAG, "onRefreshServerStatus: ${status.toString()}")
            try {
                _uiState.value = _uiState.value.copy(
                    serverStatus = status,
                    isRefreshing = false)
                Log.i(TAG, "onRefreshServerStatus: refresh bitti")
            } catch (e: Exception) {
                Log.e(TAG, "onRefreshServerStatus: ${e.message}", )
            }
        }


    }

    fun onServerTimeUpdate(){
        viewModelScope.launch {
            val time = timeRepository.
            _uiState.value = _uiState.value.copy(serverTime = )

        }
    }

    fun onTargetIPChanged(newIP: String) {
        _uiState.value = _uiState.value.copy(serverIP = newIP)
    }
}
