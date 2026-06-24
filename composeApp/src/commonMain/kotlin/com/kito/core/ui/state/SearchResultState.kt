package com.kito.core.ui.state

sealed class SearchResultState {
    object Idle : SearchResultState()
    object Empty : SearchResultState()
    object Success : SearchResultState()
}
