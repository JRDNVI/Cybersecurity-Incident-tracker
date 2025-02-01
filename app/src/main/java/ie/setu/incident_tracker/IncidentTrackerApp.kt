package ie.setu.incident_tracker

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.setu.incident_tracker.ui.navigation.IncidentTrackerNavHost

@Composable
fun IncidentTrackerApp(navController: NavHostController = rememberNavController()) {
    IncidentTrackerNavHost(navController = navController)
}

@Composable
fun IncidentTrackerBottomBar(
    navigateToHome: () -> Unit,
) {
    BottomAppBar(
        modifier = Modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        IconButton(onClick = navigateToHome) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Navigate Home"
            )
        }
    }
}