package br.com.analytics.educa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.analytics.educa.ui.route.Route
import br.com.analytics.educa.ui.screen.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Route.screenOne) {
                composable(route = Route.screenOne) {
                    ScreenOne(
                        navigateToScreenTwo = { navController.navigate(Route.screenTwo) }
                    )
                }
                composable(route = Route.screenTwo) {
                    ScreenTwo(
                        navigateToScreenThree = { navController.navigate(Route.screenThree) },
                        navigateToScreenFour = { navController.navigate(Route.screenFour) },
                        navigateToScreenFive = { navController.navigate(Route.screenFive) },
                        navigateBack = { navController.popBackStack() }
                    )
                }
                composable(route = Route.screenThree) {
                    ScreenThree(
                        navigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
