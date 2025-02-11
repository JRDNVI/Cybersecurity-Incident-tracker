package ie.setu.incident_tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ie.setu.incident_tracker.ui.home.HomeDestination
import ie.setu.incident_tracker.ui.home.HomeScreen
import ie.setu.incident_tracker.ui.incident.AddIncidentDestination
import ie.setu.incident_tracker.ui.incident.AddIncidentScreen

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
                navigateToAddIncident = { navController.navigate(AddIncidentDestination.route) },
                navigateToEditIncident = {  },
                navigateToIncidentDetails = {  }
            )
        }

        composable(route = AddIncidentDestination.route) {
            AddIncidentScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = { navController.navigate(HomeDestination.route) }
            )
        }
    }
}