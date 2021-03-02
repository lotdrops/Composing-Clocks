package com.pose.composingclocks.core.savestate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

interface StateSaver {
    fun <T> getAutoSaveFlow(scope: CoroutineScope, key: String, default: T): MutableStateFlow<T>
    fun <T> setValue(key: String, value: T)
    fun <T> getValue(key: String): T?
    fun <T> getValue(key: String, default: T): T
    fun unTrackKey(key: String)
}
