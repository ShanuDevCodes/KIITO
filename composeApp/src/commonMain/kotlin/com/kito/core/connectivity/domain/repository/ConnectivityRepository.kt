package com.kito.core.connectivity.domain.repository

import kotlinx.coroutines.flow.StateFlow

/**
 * Domain interface representing network connectivity status.
 * This decouples the presentation layer from the direct platform ConnectivityObserver.
 */
interface ConnectivityRepository {
    val isOnline: StateFlow<Boolean>
}
