package com.kito.core.ui.state

sealed class SyncUiState {
    object Idle : SyncUiState()
    object Loading : SyncUiState()
    object Success : SyncUiState()
    data class Error(val message: String) : SyncUiState()
}
