package com.pose.composingclocks.core.scopednav.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.pose.composingclocks.core.koincompat.getKoin
import com.pose.composingclocks.core.scopednav.ClearablesScopeCallback
import com.pose.composingclocks.core.scopednav.ScopeLifecycleHandler
import org.koin.core.scope.Scope

/**
 * Substitute for navigation
 * This helper function can be used for declaring nested navigation graphs when we want to have
 * a scope bound to that nested graph.
 * It is only for convenience, as it provides the NestedNavGraph that can be used by the composables
 * in that graph.
 */
inline fun <reified T : NestedNavGraph<*, *>> NavGraphBuilder.scopedNavigation(
    nestedNavGraph: T,
    crossinline builder: NavGraphBuilder.(T) -> Unit
): Unit = navigation(nestedNavGraph.startDestination.declaredPath, nestedNavGraph.declaredPath) {
    builder(nestedNavGraph)
}

/**
 * Substitute for composable
 * This helper function is used to declare any screen for which we want to have a scope.
 */
inline fun <reified T : NavDestination<*>> NavGraphBuilder.scopedComposable(
    destination: T,
    crossinline content: @Composable (NavBackStackEntry, Scope) -> Unit,
) {
    composable(destination.declaredPath, destination.namedNavArgs) { navEntry ->
        val args = navEntry.arguments
        val pathWithArgs = destination.buildPath(args)
        val koinScope = getKoin().getOrCreateScope<T>(pathWithArgs)
        koinScope.registerCallback(ClearablesScopeCallback())

        RunOnce {
            ScopeLifecycleHandler().bind(koinScope, navEntry.lifecycle)
        }

        content(navEntry, koinScope)
    }
}

/**
 * Substitute for composable in nested graphs
 * This helper function is used to declare screens in nested graphs, when we want the scope of
 * the nested graph and the scope of this screen.
 */
inline fun <reified T : NavDestination<*>, reified P : NestedNavGraph<*, *>>
NavGraphBuilder.doubleScopedComposable(
    navController: NavController,
    parentGraph: P,
    destination: T,
    crossinline content: @Composable (NavBackStackEntry, parentScope: Scope, Scope) -> Unit,
) {
    composable(destination.declaredPath) { navEntry ->
        val parentScope = getKoin().getOrCreateScope<P>(parentGraph.declaredPath)

        val args = navEntry.arguments
        val pathWithArgs = destination.buildPath(args)
        val koinScope = getKoin().getOrCreateScope<T>(pathWithArgs)

        RunOnce {
            val parentEntry = navEntry.getParentEntry(navController)
            if (parentEntry != null) {
                parentScope.getOrNull<ScopeLifecycleHandler>()?.bind(
                    scope = parentScope,
                    lifecycle = parentEntry.lifecycle,
                )
            }

            ScopeLifecycleHandler().bind(koinScope, navEntry.lifecycle)
        }

        content(navEntry, parentScope, koinScope)
    }
}

fun NavBackStackEntry.getParentEntry(navController: NavController) =
    destination.parent?.id?.let { parentId ->
        navController.getBackStackEntry(parentId)
    }

fun NavBackStackEntry.getParentOrThis(navController: NavController) =
    getParentEntry(navController) ?: this

/**
 * Hack so that an effect can be run only once and not on each composition.
 */
@Composable
fun RunOnce(composable: () -> Unit) {
    rememberSaveable {
        composable()
        true
    }
}
