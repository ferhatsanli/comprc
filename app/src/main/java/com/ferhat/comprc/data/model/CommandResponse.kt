package com.ferhat.comprc.data.model

data class CommandResponse(
    val success: Boolean,
    val output: String?,
    val error: String?
)
