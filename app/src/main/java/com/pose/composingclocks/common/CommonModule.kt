package com.pose.composingclocks.common

import com.pose.composingclocks.app.BottomNavItem
import com.pose.composingclocks.common.cities.CitiesListMockUseCase
import com.pose.composingclocks.common.clock.TimezoneFormatter
import com.pose.composingclocks.core.savestate.BundleStateSaver
import com.pose.composingclocks.feature.add.AddCityMockUseCase
import org.koin.dsl.module

val commonModule = module {
    factory { AddCityMockUseCase(get()) }
    factory { TimezoneFormatter() }
    single { (saver: BundleStateSaver) ->
        AppViewModel(saver, BottomNavItem.Config.pathRoot)
    }
    single { (saver: BundleStateSaver) -> CitiesListMockUseCase(saver, get()) }
}
