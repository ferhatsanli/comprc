package com.ferhat.comprc.domain.model

import com.ferhat.comprc.domain.model.ServerStatus

data class MainUiState(
    val serverStatus: ServerStatus = ServerStatus.NA,
    val isPowerButtonLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val serverIP: String = "http://rpi.local:8080",
    val serverTime: String = "13:35.33"
)
