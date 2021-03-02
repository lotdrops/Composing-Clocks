package com.pose.composingclocks.core.scopednav.navigation

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

interface NavDestination <T : NavParams> {
    val declaredPath: String
    val namedNavArgs: List<NamedNavArgument> get() = emptyList()
    fun buildPath(args: Bundle?): String = declaredPath
    fun buildRoute(params: T): String = declaredPath
}

interface NestedNavGraph <T : NavParams, R : NavParams> : NavDestination<T> {
    val startDestination: NavDestination<R>
}

open class ScreenDestination<T : NavParams>(
    pathRoot: String,
) : NavDestination<T> {
    override val declaredPath: String = pathRoot
}

open class ScreenDestinationWithArgs<T : NavParams>(
    private val pathRoot: String,
    private val arguments: List<NavArgument<*>> = emptyList(),
) : NavDestination<T> {
    override val declaredPath: String = arguments.asRoute(pathRoot)
    override val namedNavArgs = arguments.toNamedNavArgs()
    override fun buildPath(args: Bundle?): String = "$pathRoot${arguments.asPathWithArgs(args)}"
    override fun buildRoute(params: T): String = params.list.buildRoute(pathRoot)
}

open class SubgraphDestination<T : NavParams, R : NavParams>(
    pathRoot: String,
    override val startDestination: NavDestination<R>,
) : NavDestination<T>, NestedNavGraph<T, R> {
    override val declaredPath: String = pathRoot
}

open class SubgraphDestinationWithArgs<T : NavParams, R : NavParams>(
    pathRoot: String,
    arguments: List<NavArgument<*>> = emptyList(),
    override val startDestination: NavDestination<R>,
) : ScreenDestinationWithArgs<T>(pathRoot, arguments), NestedNavGraph<T, R>

interface NavParams {
    val list: List<String>
}

object NoParams : NavParams {
    override val list: List<String> get() = emptyList()
}

sealed class OptionalParam<out T> {
    data class Provided<T>(val value: T) : OptionalParam<T>()
    object Default : OptionalParam<Nothing>()
}

sealed class NavArgument<T> {
    abstract val key: String
    abstract val type: NavType<T>
    fun getValue(args: Bundle?): T? = args?.let { type.get(it, key) }

    data class Required<T>(
        override val key: String,
        override val type: NavType<T>,
    ) : NavArgument<T>()

    data class Optional<T>(
        override val key: String,
        override val type: NavType<T>,
        val defaultValue: T,
    ) : NavArgument<T>() {
        fun paramAsRoute(param: OptionalParam<T>): String = when (param) {
            OptionalParam.Default -> ""
            is OptionalParam.Provided -> "?$key=${param.value}"
        }
    }
}

private fun List<NavArgument<*>>.toNamedNavArgs() = map { navArg ->
    navArgument(navArg.key) {
        type = navArg.type
        if (navArg is NavArgument.Optional) defaultValue = navArg.defaultValue
    }
}

private fun List<String>.buildRoute(root: String) = fold(root) { accumulatedPath, argument ->
    "$accumulatedPath/$argument"
}
private fun List<NavArgument<*>>.asRoute(root: String) = fold(root) { accumulatedPath, argument ->
    accumulatedPath + argument.asRoute()
}

private fun NavArgument<*>.asRoute() =
    if (this is NavArgument.Optional) "/?$key={$key}" else "/{$key}"

private fun List<NavArgument<*>>.asPathWithArgs(args: Bundle?) =
    fold("") { accumulatedPath, argument ->
        accumulatedPath + argument.asRoute() + ":" + args?.let { argument.getValue(it) }
    }
