package com.pose.composingclocks.app

import com.pose.composingclocks.core.scopednav.Clearable
import com.pose.composingclocks.feature.cities.CitiesScreen
import com.pose.composingclocks.feature.cities.CitiesViewModel
import com.pose.composingclocks.feature.cities.CityDetailScreen
import com.pose.composingclocks.feature.cities.CityDetailViewModel
import com.pose.composingclocks.feature.config.ConfigScreen
import com.pose.composingclocks.feature.config.ConfigViewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

val appModule = module {

    scope<CitiesScreen> {
        scoped { (navToCity: (Int) -> Unit) ->
            CitiesViewModel(get(), navToCity)
        } bind Clearable::class
    }
    scope<CityDetailScreen> {
        scoped { (cityId: Int, navBack: () -> Unit) ->
            CityDetailViewModel(cityId, get(), get(), navBack)
        } bind Clearable::class
    }
    scope<ConfigScreen> {
        scoped { ConfigViewModel() } bind Clearable::class
    }
    scope<MainActivity> {
    }
}

val getActivityScope = { getKoin().getOrCreateScope<MainActivity>("MainActivity") }
