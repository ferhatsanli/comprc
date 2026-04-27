package com.ferhat.comprc.presentation

import androidx.lifecycle.ViewModel
import com.ferhat.comprc.domain.model.MainUiState
import com.ferhat.comprc.domain.model.ServerStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onPowerButtonClick() {
        // Burada power komutu gönderme işlemi başlatılır
        // isPowerButtonLoading = true yapılır, sonra false'a çekilir
    }

    fun onRefreshServerStatus() {
        // Burada sunucu durumu sorgulanır ve serverStatus güncellenir
    }
}
