package com.pose.composingclocks.common.cities

import com.pose.composingclocks.core.savestate.StateSaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val UserListKey = "UserList_CitiesUseCaseStateSaverKey"

class CitiesListMockUseCase(saver: StateSaver, scope: CoroutineScope) {
    val userCities = saver.getAutoSaveFlow(scope, UserListKey, emptyList<City>())

    operator fun invoke(): Flow<List<City>> = userCities.map { it + mockData }
}

private const val BARCELONA_IMG = "https://images.unsplash.com/photo-1560533323-f72f1f8e58b2?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80"
private const val PARIS_IMG = "https://images.unsplash.com/photo-1518057509104-47943ca12e4f?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=935&q=80"
private const val TOKYO_IMG = "https://images.unsplash.com/photo-1513407030348-c983a97b98d8?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=1952&q=80"
private const val SEATTLE_IMG = "https://images.unsplash.com/photo-1502175353174-a7a70e73b362?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=3070&q=80"
private const val SYDNEY_IMG = "https://images.unsplash.com/photo-1506374322094-6021fc3926f1?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=2006&q=80"

private const val BARCELONA_DESC = "Barcelona is a city on the coast of northeastern Spain. It is the capital and largest city of the autonomous community of Catalonia, as well as the second most populous municipality of Spain. With a population of 1.6 million within city limits, its urban area extends to numerous neighbouring municipalities within the Province of Barcelona and is home to around 4.8 million people,[3] making it the fifth most populous urban area in the European Union after Paris, the Ruhr area, Madrid, and Milan.[3] It is one of the largest metropolises on the Mediterranean Sea, located on the coast between the mouths of the rivers Llobregat and Besòs, and bounded to the west by the Serra de Collserola mountain range, the tallest peak of which is 512 metres (1,680 feet) high."
private const val PARIS_DESC = "Paris is the capital and most populous city of France, with an estimated population of 2,148,271 residents as of 2020, in an area of more than 105 square kilometres (41 square miles). Since the 17th century, Paris has been one of Europe's major centres of finance, diplomacy, commerce, fashion, science and arts. The City of Paris is the centre and seat of government of the Île-de-France, or Paris Region, which has an estimated official 2020 population of 12,278,210, or about 18 percent of the population of France. The Paris Region had a GDP of €709 billion (" +
    "$808 billion) in 2017. According to the Economist Intelligence Unit Worldwide Cost of Living Survey in 2018, Paris was the second most expensive city in the world, after Singapore, and ahead of Zürich, Hong Kong, Oslo and Geneva. Another source ranked Paris as most expensive, on a par with Singapore and Hong Kong, in 2018."
private const val TOKYO_DESC = "Tokyo officially Tokyo Metropolis (東京都, Tōkyō-to), is the capital and most populous prefecture of Japan. Located at the head of Tokyo Bay, the prefecture forms part of the Kantō region on the central Pacific coast of Japan’s main island of Honshu. Tokyo is the political and economic center of the country, as well as the seat of the Emperor of Japan and the national government. In 2019, the prefecture had an estimated population of 13,929,280. The Greater Tokyo Area is the most populous metropolitan area in the world, with more than 37.393 million residents as of 2020."
private const val SEATTLE_DESC = "Seattle is a seaport city on the West Coast of the United States. It is the seat of King County, Washington. Seattle is the largest city in both the state of Washington and the Pacific Northwest region of North America. According to U.S. Census data released in 2019, the Seattle metropolitan area's population stands at 3.98 million, making it the 15th-largest in the United States. In July 2013, Seattle was the fastest-growing major city in the United States and remained in the top five in May 2015 with an annual growth rate of 2.1%. In July 2016, Seattle ranked as the fastest-growing major U.S. city, with a 3.1% annual growth rate.\n" +
    "Seattle is situated on an isthmus between Puget Sound (an inlet of the Pacific Ocean) and Lake Washington. It is the northernmost large city in the United States, located about 100 miles (160 km) south of the Canadian border. A major gateway for trade with Asia, Seattle is the fourth-largest port in North America in terms of container handling as of 2015."
private const val SYDNEY_DESC = "Sydney is the capital city of the state of New South Wales, and the most populous city in Australia and Oceania. Located on Australia's east coast, the metropolis surrounds Port Jackson and extends about 70 km (43.5 mi) on its periphery towards the Blue Mountains to the west, Hawkesbury to the north, the Royal National Park to the south and Macarthur to the south-west. Sydney is made up of 658 suburbs, spread across 33 local government areas. Informally there are at least 15 regions. Residents of the city are known as \"Sydneysiders\". As of June 2019, Sydney's estimated metropolitan population was 5,312,163, meaning the city is home to approximately 65% of the state's population."

val mockData = listOf(
    City(0, "Barcelona", BARCELONA_DESC, "Europe/Madrid", BARCELONA_IMG),
    City(1, "Paris", PARIS_DESC, "Europe/Paris", PARIS_IMG),
    City(2, "Tokyo", TOKYO_DESC, "Asia/Tokyo", TOKYO_IMG),
    City(3, "Seattle", SEATTLE_DESC, "America/Los_Angeles", SEATTLE_IMG),
    City(4, "Sydney", SYDNEY_DESC, "Australia/Sydney", SYDNEY_IMG),
)
