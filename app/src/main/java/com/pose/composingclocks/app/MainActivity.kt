package com.pose.composingclocks.app

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pose.composingclocks.app.theme.ComposingClocksTheme
import com.pose.composingclocks.common.AppViewModel
import com.pose.composingclocks.common.cities.CitiesListMockUseCase
import com.pose.composingclocks.common.clock.ClockTheme
import com.pose.composingclocks.core.savestate.BundleStateSaver
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf
import java.util.Locale

val LocalClockTheme = compositionLocalOf<ClockTheme> { error("No clock theme found!") }

private const val StateSaverKey = "GlobalStateSaverKey"

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val stateSaver = rememberSaveable(
                key = StateSaverKey,
                saver = BundleStateSaver.saver,
            ) { BundleStateSaver(Bundle()) }
            val appVm: AppViewModel = get { parametersOf(stateSaver) }
            get<CitiesListMockUseCase> { parametersOf(stateSaver) }

            val language: String by appVm.language.collectAsState()
            ComposingClocksTheme {
                val clockTheme: ClockTheme by appVm.currentClockTheme.collectAsState()
                CompositionLocalProvider(LocalClockTheme provides clockTheme) {
                    AppLayout(language)
                }
            }
        }
    }

    override fun onDestroy() {
        getActivityScope().close()
        super.onDestroy()
    }
}

/**
 * This uses updateConfiguration which is deprecated. "Traditional-Android" ways of changing
 * the locale require recreating the activity, except for this one which is deprecated.
 * The LanguageOverride method lets us achieve the same in a more "compose way". But it breaks
 * navigation. Until that is sorted out we have the deprecated method.
 */
@Composable
private fun SetLanguage(language: String) {
    val locale = Locale(language)
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

/**
 * Setting the context provider breaks the navigation backstack: pressing back on any screen exits
 * the app instead of going to the previous screen
 * Not using this method until there is a solution to this
 */
@Composable
private fun LanguageOverride(
    language: String,
    activity: ComponentActivity,
    content: @Composable () -> Unit
) {
    val locale = Locale(language)
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val context = LocalContext.current.createConfigurationContext(configuration)
    val direction = if (TextUtils.getLayoutDirectionFromLocale(locale) ==
        View.LAYOUT_DIRECTION_RTL
    ) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalOnBackPressedDispatcherOwner provides activity,
        LocalConfiguration provides configuration,
        LocalLayoutDirection provides direction,
        LocalContext provides context,
    ) {
        content()
    }
}

@ExperimentalAnimationApi
@Composable
fun AppLayout(language: String) {
    SetLanguage(language = language)
    val navController = rememberAnimatedNavController()
    Scaffold(bottomBar = { BottomBar(navController) }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            RootNavigation(navController)
        }
    }
}
