package com.ferhat.comprc.viewmodel

import com.ferhat.comprc.data.model.CommandResponse

data class CommandUiState(
    val isLoading: Boolean = false,
    val response: CommandResponse? = null,
    val error: String? = null
    )
