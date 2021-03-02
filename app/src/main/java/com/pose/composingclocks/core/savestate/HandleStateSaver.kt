package com.pose.composingclocks.core.savestate

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HandleStateSaver(private val savedStateHandle: SavedStateHandle) : StateSaver {
    private val trackedValues = mutableMapOf<String, Job>()

    override fun <T> getAutoSaveFlow(
        scope: CoroutineScope,
        key: String,
        default: T
    ): MutableStateFlow<T> {
        val flow = MutableStateFlow(getValue(key, default))
        val job = scope.launch {
            flow.collect { setValue(key, it) }
        }
        unTrackKey(key)
        trackedValues[key] = job
        return flow
    }

    override fun <T> setValue(key: String, value: T) = savedStateHandle.set(key, value)

    override fun <T> getValue(key: String): T? = savedStateHandle.get<T>(key)

    override fun <T> getValue(key: String, default: T): T = getValue(key) ?: default

    override fun unTrackKey(key: String) {
        trackedValues.remove(key)?.cancel()
    }
}
