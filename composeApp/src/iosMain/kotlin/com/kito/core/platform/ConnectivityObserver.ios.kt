package com.kito.core.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_monitor_t
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_queue_t

actual class ConnectivityObserver {
    private val _isOnline = MutableStateFlow(true)
    actual val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        val monitor: nw_path_monitor_t = nw_path_monitor_create()
        val queue: dispatch_queue_t = dispatch_get_global_queue(0.toLong(), 0.toULong())

        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            _isOnline.value = status == nw_path_status_satisfied
        }

        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }
}
