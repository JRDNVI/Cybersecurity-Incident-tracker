package ie.setu.incident_tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.incident_tracker.ui.home.HomeDestination
import ie.setu.incident_tracker.ui.home.HomeScreen

@Composable
fun IncidentTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddIncident = {  },
                navigateToEditIncident = {  },
                navigateToAddIncidentDetails ={  }
            )
        }
    }
}