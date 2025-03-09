package ie.setu.incident_tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ie.setu.incident_tracker.ui.auth.SignInDestination
import ie.setu.incident_tracker.ui.auth.SignInScreen
import ie.setu.incident_tracker.ui.auth.SignUpDestination
import ie.setu.incident_tracker.ui.auth.SignUpScreen
import ie.setu.incident_tracker.ui.device.AddDeviceDestination
import ie.setu.incident_tracker.ui.device.AddDeviceScreen
import ie.setu.incident_tracker.ui.device.EditDeviceDestination
import ie.setu.incident_tracker.ui.device.EditDeviceScreen
import ie.setu.incident_tracker.ui.home.HomeDestination
import ie.setu.incident_tracker.ui.home.HomeScreen
import ie.setu.incident_tracker.ui.incident.AddIncidentDestination
import ie.setu.incident_tracker.ui.incident.AddIncidentScreen
import ie.setu.incident_tracker.ui.incident.EditIncidentDestination
import ie.setu.incident_tracker.ui.incident.EditIncidentScreen
import ie.setu.incident_tracker.ui.incident.ViewIncidentDetailsDestination
import ie.setu.incident_tracker.ui.incident.ViewIncidentDetailsScreen

@Composable
fun IncidentTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SignInDestination.route,
        modifier = modifier
    ) {

        composable(route = SignInDestination.route) {
            SignInScreen(
                navigateToHomeScreen = { navController.navigate(HomeDestination.route) },
                navigateToSignUpScreen = { navController.navigate(SignUpDestination.route) }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddIncident = { navController.navigate(AddIncidentDestination.route) },
                navigateToEditIncident = { navController.navigate("${EditIncidentDestination.route}/${it}") },
                navigateToIncidentDetails = { navController.navigate("${ViewIncidentDetailsDestination.route}/${it}")}
            )
        }

        composable(route = AddIncidentDestination.route) {
            AddIncidentScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = { navController.navigate(HomeDestination.route) }
            )
        }
        composable(route = SignUpDestination.route) {
            SignUpScreen(
                navigateToHomeScreen = { navController.navigate(HomeDestination.route) },
                navigateBack = { navController.navigate(SignInDestination.route) }
            )
        }
        composable(
            route = ViewIncidentDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ViewIncidentDetailsDestination.incidentIdArg) {
                type = NavType.IntType
            })
        ) {
            ViewIncidentDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToAddDevice = { navController.navigate("${AddDeviceDestination.route}/${it}") },
                navigateToEditDevice = { navController.navigate("${EditDeviceDestination.route}/${it}") }
            )
        }

        composable(
            route = EditDeviceDestination.routeWithArgs,
            arguments = listOf(navArgument(EditDeviceDestination.deivceIdArg) {
                type = NavType.IntType
            })
        ) {
            EditDeviceScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = EditIncidentDestination.routeWithArgs,
            arguments = listOf(navArgument(EditIncidentDestination.incidentIdArg) {
                type = NavType.IntType
            })
        ) {
            EditIncidentScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
    }

        composable(
            route = AddDeviceDestination.routeWithArgs,
            arguments = listOf(navArgument(AddDeviceDestination.IncidentIDArg) {
                type = NavType.IntType
            })
        ) {
            AddDeviceScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.navigate(HomeDestination.route) }

            )
        }
    }
}