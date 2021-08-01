package com.pose.composingclocks.app

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pose.composingclocks.R
import com.pose.composingclocks.common.AppViewModel
//import com.pose.composingclocks.core.koincompat.get
import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.doubleScopedComposable
import com.pose.composingclocks.core.scopednav.navigation.getParentOrThis
import com.pose.composingclocks.core.scopednav.navigation.getWith
import com.pose.composingclocks.core.scopednav.navigation.scopedComposable
import com.pose.composingclocks.core.scopednav.navigation.scopedNavigation
import com.pose.composingclocks.feature.add.AddCityFlowViewModel
import com.pose.composingclocks.feature.add.AddCityMainScreen
import com.pose.composingclocks.feature.add.AddCityMainViewModel
import com.pose.composingclocks.feature.add.AddCitySecondaryScreen
import com.pose.composingclocks.feature.add.AddCitySecondaryViewModel
import com.pose.composingclocks.feature.add.AddCitySubgraph
import com.pose.composingclocks.feature.cities.CitiesScreen
import com.pose.composingclocks.feature.cities.CitiesViewModel
import com.pose.composingclocks.feature.cities.CityDetailScreen
import com.pose.composingclocks.feature.cities.CityDetailValues
import com.pose.composingclocks.feature.config.ConfigScreen
import com.pose.composingclocks.feature.config.ConfigViewModel
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

private val startDestinationPath = BottomNavItem.Config.pathRoot

@ExperimentalAnimationApi
@Composable
fun RootNavigation(navController: NavHostController) {
    val appVm: AppViewModel = get()

    NavHost(navController = navController, startDestination = startDestinationPath) {
        scopedComposable(ConfigScreen) { _, scope ->
            val vm: ConfigViewModel = scope.get()
            val appVm: AppViewModel = get()
            ConfigScreen(vm, appVm)
        }

        scopedComposable(CitiesScreen) { _, scope ->
            val navigate = { id: Int ->
                navController.navigate(CityDetailScreen.buildRoute(CityDetailValues(id)))
            }
            val viewModel = scope.getWith<CitiesViewModel>(navigate)
            CitiesScreen(viewModel)
        }
        scopedComposable(CityDetailScreen) { navBackStackEntry, scope ->
            CityDetailScreen.getId(navBackStackEntry.arguments)?.let { cityId ->
                CityDetailScreen(scope.getWith(cityId, { navController.navigateUp() }))
            }
        }

        scopedNavigation(AddCitySubgraph) { nestedNavGraph ->
            val onAddDone: () -> Unit = {
                navToTopDestination(CitiesScreen.buildRoute(NoParams), navController, appVm)
            }

            val flowVmGetter: (Scope, NavBackStackEntry) -> AddCityFlowViewModel =
                { parentScope, childNavEntry ->
                    parentScope.getWith(
                        childNavEntry.getParentOrThis(navController).savedStateHandle,
                        onAddDone,
                    )
                }

            doubleScopedComposable(
                navController, nestedNavGraph, AddCityMainScreen
            ) { navEntry, parentScope, scope ->
                val navigate = { navController.navigate(AddCitySecondaryScreen.buildRoute(NoParams)) }
                val parentVm = flowVmGetter(parentScope, navEntry)
                val viewModel = scope.get<AddCityMainViewModel> {
                    parametersOf(parentVm, navigate)
                }

                AddCityMainScreen(viewModel)
            }

            doubleScopedComposable(
                navController, nestedNavGraph, AddCitySecondaryScreen
            ) { navEntry, parentScope, scope ->
                val navBack = { navController.navigateUp() }
                val parentVm = flowVmGetter(parentScope, navEntry)
                val viewModel = scope.get<AddCitySecondaryViewModel> {
                    parametersOf(parentVm, navBack)
                }

                AddCitySecondaryScreen(viewModel)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val appVm: AppViewModel = get()
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val bottomBarSelection: String by appVm.bottomBarSelection.collectAsState()

        val items = listOf(
            BottomNavItem.Config,
            BottomNavItem.Cities,
            BottomNavItem.Add,
        )

        updateStateIfStartDestination(navBackStackEntry?.destination, appVm)

        items.forEach { screen ->
            val route = screen.pathRoot

            BottomNavigationItem(
                icon = { Icon(screen.icon, stringResource(screen.title)) },
                label = { Text(stringResource(screen.title)) },
                selected = bottomBarSelection == route,
                onClick = {
                    appVm.bottomBarSelection.value = route
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun navToTopDestination(
    path: String,
    navController: NavController,
    appViewModel: AppViewModel,
) {
    navController.navigate(path) {
        popUpTo(navController.graph.startDestinationId) {}
    }
    appViewModel.onBottomDestinationChanged(path)
}

private fun updateStateIfStartDestination(destination: NavDestination?, vm: AppViewModel) {
    if (destination?.id != null && destination.id == destination.getRootGraph()?.startDestinationId) {
        vm.onBottomDestinationChanged(startDestinationPath)
    }
}

private fun NavDestination.getRootGraph(): NavGraph? {
    var parentGraph = parent
    while (parentGraph?.parent != null) {
        parentGraph = parentGraph.parent
    }
    return parentGraph
}

sealed class BottomNavItem(
    val pathRoot: String,
    @StringRes val title: Int,
    val icon: ImageVector,
) {
    object Config : BottomNavItem(
        ConfigScreen.declaredPath,
        R.string.bottom_nav_config,
        Icons.Filled.Settings,
    )
    object Cities : BottomNavItem(
        CitiesScreen.declaredPath,
        R.string.bottom_nav_cities,
        Icons.Filled.List,
    )
    object Add : BottomNavItem(
        AddCitySubgraph.declaredPath,
        R.string.bottom_nav_add,
        Icons.Filled.Add,
    )
}
