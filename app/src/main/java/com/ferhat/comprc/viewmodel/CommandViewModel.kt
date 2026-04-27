package com.ferhat.comprc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferhat.comprc.data.repository.CommandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommandViewModel(private val repository: CommandRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CommandUiState())
    val uiState: StateFlow<CommandUiState> = _uiState.asStateFlow()

    fun SendCommand(command: String) {
        viewModelScope.launch{
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = repository.executeCommand(command)

            result.onSuccess { response ->
                _uiState.update { it.copy(isLoading = false, response = response) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Unknown error")}
            }
        }
    }
}