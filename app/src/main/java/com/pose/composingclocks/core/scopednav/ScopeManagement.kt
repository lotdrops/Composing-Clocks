package com.pose.composingclocks.core.scopednav

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback

class ScopeLifecycleHandler {
    private var boundToLifecycle = false

    fun bind(scope: Scope, lifecycle: Lifecycle) {
        if (!boundToLifecycle) {
            boundToLifecycle = true
            scope.registerCallback(ClearablesScopeCallback())
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        scope.close()
                    }
                }
            })
        }
    }
}

class ClearablesScopeCallback : ScopeCallback {
    override fun onScopeClose(scope: Scope) {
        scope.getAll<Clearable>().forEach { it.clear() }
    }
}
