package com.ferhat.comprc.domain.model

enum class ServerStatus {
    NA, OK, BAD
}

data class MainUiState(
    val serverStatus: ServerStatus = ServerStatus.NA,
    val isPowerButtonLoading: Boolean = false
)

