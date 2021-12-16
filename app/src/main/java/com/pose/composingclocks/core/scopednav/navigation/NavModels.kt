package com.pose.composingclocks.core.scopednav.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.pose.composingclocks.app.NavTransition

interface NavDestination <T : NavParams> {
    val pathRoot: String
    val declaredPath: String
    val namedNavArgs: List<NamedNavArgument> get() = listOf(NavArgTransition).toNamedNavArgs()
    fun buildPath(args: Bundle?): String =
        buildPathWithArgs(pathRoot, listOf(NavArgTransition), args)
    fun buildRoute(params: T, transition: NavTransition = NavTransition.Default): String =
        "$pathRoot${transition.toRoute()}"
}

interface NestedNavGraph <T : NavParams, R : NavParams> : NavDestination<T> {
    val startDestination: NavDestination<R>
}

open class ScreenDestination<T : NavParams>(
    override val pathRoot: String,
) : NavDestination<T> {
    override val declaredPath: String = listOf(NavArgTransition).asRoute(pathRoot)
}

open class ScreenDestinationWithArgs<T : NavParams>(
    override val pathRoot: String,
    private val arguments: List<NavArgument<*>> = emptyList(),
) : NavDestination<T> {
    private val allArguments = arguments + NavArgTransition
    override val declaredPath: String = allArguments.asRoute(pathRoot)
    override val namedNavArgs = allArguments.toNamedNavArgs()
    override fun buildPath(args: Bundle?): String = buildPathWithArgs(pathRoot, allArguments, args)
    override fun buildRoute(params: T, transition: NavTransition): String =
        (params.list + transition.toString()).buildRoute(pathRoot)
}

private fun buildPathWithArgs(
    pathRoot: String,
    arguments: List<NavArgument<*>>,
    bundle: Bundle?,
) = "$pathRoot${arguments.asPathWithArgs(bundle)}"

open class SubgraphDestination<T : NavParams, R : NavParams>(
    override val pathRoot: String,
    override val startDestination: NavDestination<R>,
) : NavDestination<T>, NestedNavGraph<T, R> {
    override val declaredPath: String = listOf(NavArgTransition).asRoute(pathRoot)
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
    fun getValue(args: Bundle?): T? = args?.let { type[it, key] }

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

private object NavArgTransition : NavArgument<NavTransition>() {
    override val key: String = NavTransition.ARG_KEY
    override val type: NavType<NavTransition> = NavType.EnumType(NavTransition::class.java)
}

private fun NavTransition?.toRoute(): String = if (this == null) "" else "/$this"

private fun List<NavArgument<*>>.toNamedNavArgs() = map { navArg ->
    navArgument(navArg.key) {
        type = navArg.type
        if (navArg is NavArgument.Optional) defaultValue = navArg.defaultValue
    }
}

private fun List<String>.buildRoute(root: String): String = fold(root) { accPath, argument ->
    "$accPath/$argument"
}
private fun List<NavArgument<*>>.asRoute(root: String): String = fold(root) { accPath, argument ->
    accPath + argument.asRoute()
}

private fun NavArgument<*>.asRoute(): String =
    if (this is NavArgument.Optional) "/?$key={$key}" else "/{$key}"

private fun List<NavArgument<*>>.asPathWithArgs(args: Bundle?): String =
    fold("") { accumulatedPath, argument ->
        accumulatedPath + argument.asRoute() + ":" + args?.let { argument.getValue(it) }
    }
