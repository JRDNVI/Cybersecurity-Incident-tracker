package ie.setu.incident_tracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import ie.setu.incident_tracker.ui.auth.login.LoginScreen
import ie.setu.incident_tracker.ui.auth.login.LoginScreenDestination
import ie.setu.incident_tracker.ui.auth.register.RegisterScreen
import ie.setu.incident_tracker.ui.auth.register.RegisterScreenDestination
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
import ie.setu.incident_tracker.ui.profile.ProfileScreen
import ie.setu.incident_tracker.ui.profile.ProfileScreenDestination

@Composable
fun IncidentTrackerNavHost(
    navController: NavHostController,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination.route,
        modifier = modifier
    ) {

        composable(route = LoginScreenDestination.route) {
            LoginScreen(
                navigateToRegisterScreen = { navController.navigate(RegisterScreenDestination.route) },
                navigateToHomeScreen = { navController.navigate(HomeDestination.route)}
            )
        }

        composable(route = RegisterScreenDestination.route) {
            RegisterScreen(
                navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
            )
        }

        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddIncident = { navController.navigate(AddIncidentDestination.route) },
                navigateToEditIncident = { navController.navigate("${EditIncidentDestination.route}/${it}") },
                navigateToIncidentDetails = { navController.navigate("${ViewIncidentDetailsDestination.route}/${it}")},
                navigateToSignInScreen = { navController.navigate(LoginScreenDestination.route) },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }

        composable(route = ProfileScreenDestination.route) {
            ProfileScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = { navController.navigate(HomeDestination.route) },
                onSignOut = { navController.navigate(LoginScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }

        composable(route = AddIncidentDestination.route) {
            AddIncidentScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateHome = { navController.navigate(HomeDestination.route) },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode

            )
        }

        composable(
            route = ViewIncidentDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ViewIncidentDetailsDestination.incidentIdArg) {
                type = NavType.StringType
            })
        ) {
            ViewIncidentDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToAddDevice = { incidentId ->
                    navController.navigate("${AddDeviceDestination.route}/$incidentId")
                },
                navigateToEditDevice = { incidentId, deviceId ->
                    navController.navigate("edit_device/$incidentId/$deviceId")
                },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }


        composable(
            route = EditDeviceDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditDeviceDestination.incidentIdArg) { type = NavType.StringType },
                navArgument(EditDeviceDestination.deivceIdArg) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val incidentId = backStackEntry.arguments?.getString(EditDeviceDestination.incidentIdArg)
            val deviceId = backStackEntry.arguments?.getString(EditDeviceDestination.deivceIdArg)

            requireNotNull(incidentId)
            requireNotNull(deviceId)

            EditDeviceScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }


        composable(
            route = EditIncidentDestination.routeWithArgs,
            arguments = listOf(navArgument(EditIncidentDestination.incidentIdArg) {
                type = NavType.StringType
            })
        ) {
            EditIncidentScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }


        composable(
            route = AddDeviceDestination.routeWithArgs,
            arguments = listOf(navArgument(AddDeviceDestination.IncidentIDArg) {
                type = NavType.StringType
            })
        ) {
            AddDeviceScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToProfile = {navController.navigate(ProfileScreenDestination.route)},
                onToggleDarkMode = onToggleDarkMode
            )
        }
    }
}