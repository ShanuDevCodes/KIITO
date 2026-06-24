package com.kito.core.connectivity.data

import com.kito.core.connectivity.domain.repository.ConnectivityRepository
import com.kito.core.platform.ConnectivityObserver
import kotlinx.coroutines.flow.StateFlow

/**
 * Platform/data layer implementation of ConnectivityRepository.
 * Delegates directly to the platform ConnectivityObserver.
 */
class ConnectivityRepositoryImpl(
    private val connectivityObserver: ConnectivityObserver
) : ConnectivityRepository {
    override val isOnline: StateFlow<Boolean> = connectivityObserver.isOnline
}
