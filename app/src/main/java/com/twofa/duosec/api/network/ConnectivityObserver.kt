package com.twofa.duosec.api.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<ConnectivityStatus>
}