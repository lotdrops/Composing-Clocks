package com.pose.composingclocks.core.scopednav

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Class to be used for UI presenters and viewModels.
 * Clear will be automatically called if the ViewController is declared in a Koin scope bound to
 * navigation. It should be declared as follows:
 * ```
 * scope<T> {
 *     scoped { MyViewController() } bind Clearable::class
 * }
 * ```
 */
open class ViewController : Clearable {
    protected val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun clear() {
        coroutineScope.cancel()
    }
}
