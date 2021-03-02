package com.pose.composingclocks.core.savestate

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable

private const val TypeUnsupported = "Type not supported. Provide support or change the type"

class BundleStateSaver(val bundle: Bundle) : StateSaver {
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

    override fun <T> setValue(key: String, value: T) = when (value) {
        is Int -> bundle.putInt(key, value)
        is String -> bundle.putString(key, value)
        is Long -> bundle.putLong(key, value)
        is Float -> bundle.putFloat(key, value)
        is Parcelable -> bundle.putParcelable(key, value)
        is Serializable -> bundle.putSerializable(key, value)
        // TODO support more types
        else -> throw IllegalArgumentException(TypeUnsupported)
    }

    override fun <T> getValue(key: String): T? = try {
        bundle.get(key) as T?
    } catch (e: ClassCastException) {
        throw IllegalArgumentException(TypeUnsupported)
    }

    override fun <T> getValue(key: String, default: T): T = getValue(key) ?: default

    override fun unTrackKey(key: String) {
        trackedValues.remove(key)?.cancel()
    }

    companion object {
        val saver = Saver<BundleStateSaver, Bundle>(
            save = { it.bundle },
            restore = { BundleStateSaver(it) }
        )
    }
}
