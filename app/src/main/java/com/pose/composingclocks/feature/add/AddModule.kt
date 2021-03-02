package com.pose.composingclocks.feature.add

import androidx.lifecycle.SavedStateHandle
import com.pose.composingclocks.core.savestate.HandleStateSaver
import com.pose.composingclocks.core.savestate.StateSaver
import com.pose.composingclocks.core.scopednav.Clearable
import com.pose.composingclocks.core.scopednav.navigation.navGraphScope
import org.koin.dsl.bind
import org.koin.dsl.module

val addModule = module {
    navGraphScope<AddCitySubgraph> {
        scoped { (savedState: SavedStateHandle, onDone: () -> Unit) ->
            AddCityFlowViewModel(savedState.buildSaver(), get(), onDone)
        } bind Clearable::class
    }
    scope<AddCitySecondaryScreen> {
        scoped { (parentVm: AddCityFlowViewModel, goBack: () -> Unit) ->
            AddCitySecondaryViewModel(parentVm, goBack)
        } bind Clearable::class
    }
    scope<AddCityMainScreen> {
        scoped { (parentVm: AddCityFlowViewModel, nextScreen: () -> Unit) ->
            AddCityMainViewModel(parentVm, nextScreen, get())
        } bind Clearable::class
    }
}

private fun SavedStateHandle.buildSaver() = HandleStateSaver(this) as StateSaver
