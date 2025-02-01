package ie.setu.incident_tracker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.setu.incident_tracker.ui.navigation.IncidentTrackerNavHost

@Composable
fun IncidentTrackerApp(navController: NavHostController = rememberNavController()) {
    IncidentTrackerNavHost(navController = navController)
}