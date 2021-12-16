package com.pose.composingclocks.app

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import com.pose.composingclocks.app.theme.MotionTransition

enum class NavTransition(val specs: (motionValues: MotionTransition) -> NavTransitionSpecs) {
    Default({ motionValues -> defaultSpecs(motionValues) }),
    FadeThrough({ motionValues -> fadeThroughSpecs(motionValues) }),
    SharedAxisX({ motionValues -> sharedAxisXSpecs(motionValues) }),
    SharedAxisY({ motionValues -> sharedAxisYSpecs(motionValues) }),
    SharedAxisZ({ motionValues -> sharedAxisZSpecs(motionValues) }),
    SharedAxisZFade({ motionValues -> sharedAxisZFadeSpecs(motionValues) });

    companion object {
        const val ARG_KEY = "nav_transition"
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.getEnterTransition(motionValues: MotionTransition) =
    targetState.getTransition().specs(motionValues).enterTransition

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.getExitTransition(motionValues: MotionTransition) =
    targetState.getTransition().specs(motionValues).exitTransition

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.getPopEnterTransition(motionValues: MotionTransition) =
    initialState.getTransition().specs(motionValues).popEnterTransition

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.getPopExitTransition(
    motionValues: MotionTransition
): ExitTransition = initialState.getTransition().specs(motionValues).popExitTransition

private fun NavBackStackEntry.getTransition() =
    when (val transition = (arguments?.get(NavTransition.ARG_KEY))) {
        is String -> try {
            NavTransition.valueOf(transition)
        } catch (e: Throwable) { NavTransition.Default }
        is NavTransition -> transition
        else -> NavTransition.Default
    }

data class NavTransitionSpecs(
    val enterTransition: EnterTransition,
    val exitTransition: ExitTransition,
    val popEnterTransition: EnterTransition,
    val popExitTransition: ExitTransition,
)

private fun defaultSpecs(motionValues: MotionTransition) = fadeThroughSpecs(motionValues)

@OptIn(ExperimentalAnimationApi::class)
private fun fadeThroughSpecs(motionValues: MotionTransition) = NavTransitionSpecs(
    enterTransition = scaleIn(
        initialScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceDelayedAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    exitTransition = fadeOut(animationSpec = motionValues.exitAnimationSpec()),
    popEnterTransition = scaleIn(
        initialScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceDelayedAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    popExitTransition = fadeOut(animationSpec = motionValues.exitAnimationSpec()),
)

@OptIn(ExperimentalAnimationApi::class)
private fun sharedAxisXSpecs(motionValues: MotionTransition) = NavTransitionSpecs(
    enterTransition = slideIn(
        initialOffset = { IntOffset(motionValues.transitionSlide, 0) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    exitTransition = slideOut(
        targetOffset = { IntOffset(-motionValues.transitionSlide, 0) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
    popEnterTransition = slideIn(
        initialOffset = { IntOffset(-motionValues.transitionSlide, 0) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    popExitTransition = slideOut(
        targetOffset = { IntOffset(motionValues.transitionSlide, 0) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
)

@OptIn(ExperimentalAnimationApi::class)
private fun sharedAxisYSpecs(motionValues: MotionTransition) = NavTransitionSpecs(
    enterTransition = slideIn(
        initialOffset = { IntOffset(0, motionValues.transitionSlide) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    exitTransition = slideOut(
        targetOffset = { IntOffset(0, -motionValues.transitionSlide) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
    popEnterTransition = slideIn(
        initialOffset = { IntOffset(0, -motionValues.transitionSlide) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    popExitTransition = slideOut(
        targetOffset = { IntOffset(0, motionValues.transitionSlide) },
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
)

@OptIn(ExperimentalAnimationApi::class)
private fun sharedAxisZSpecs(motionValues: MotionTransition) = NavTransitionSpecs(
    enterTransition = scaleIn(
        initialScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    exitTransition = scaleOut(
        targetScale = motionValues.transitionTargetScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
    popEnterTransition = scaleIn(
        initialScale = motionValues.transitionTargetScale,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = motionValues.entranceDelayedAnimationSpec()),
    popExitTransition = scaleOut(
        targetScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(animationSpec = motionValues.exitAnimationSpec()),
)

@OptIn(ExperimentalAnimationApi::class)
private fun sharedAxisZFadeSpecs(motionValues: MotionTransition) = NavTransitionSpecs(
    enterTransition = scaleIn(
        initialScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeIn(
        animationSpec = tween(
            motionValues.durationFraction(Z_FADE_FRACTION),
            delayMillis = motionValues.durationFraction(Z_FADE_FRACTION),
            easing = LinearEasing,
        )
    ),
    exitTransition = scaleOut(
        targetScale = motionValues.transitionTargetScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ),
    popEnterTransition = scaleIn(
        initialScale = motionValues.transitionTargetScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ),
    popExitTransition = scaleOut(
        targetScale = motionValues.transitionInitialScale,
        animationSpec = motionValues.entranceAnimationSpec(),
    ) + fadeOut(
        animationSpec = tween(
            motionValues.durationFraction(Z_FADE_FRACTION),
            delayMillis = motionValues.durationFraction(Z_FADE_FRACTION),
            easing = LinearEasing,
        )
    ),
)

private fun <T> MotionTransition.entranceAnimationSpec() = tween<T>(
    transitionDuration.toInt(),
    easing = FastOutSlowInEasing,
)

private fun <T> MotionTransition.entranceDelayedAnimationSpec() = tween<T>(
    durationMillis = durationFraction(ENTRANCE_FRACTION),
    delayMillis = (transitionDuration * EXIT_FRACTION).toInt(),
    easing = LinearOutSlowInEasing,
)

private fun <T> MotionTransition.exitAnimationSpec() = tween<T>(
    durationMillis = durationFraction(EXIT_FRACTION),
    easing = FastOutLinearInEasing,
)

private fun MotionTransition.durationFraction(fraction: Double) =
    (transitionDuration * fraction).toInt()

private const val ENTRANCE_FRACTION = 0.7
private const val EXIT_FRACTION = 0.3
private const val Z_FADE_FRACTION = 0.2
