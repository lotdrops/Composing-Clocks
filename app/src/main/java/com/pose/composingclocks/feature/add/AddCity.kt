package com.pose.composingclocks.feature.add

import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.SubgraphDestination

object AddCitySubgraph : SubgraphDestination<NoParams, NoParams>(
    pathRoot = "addCityScreen",
    startDestination = AddCityMainScreen,
)
