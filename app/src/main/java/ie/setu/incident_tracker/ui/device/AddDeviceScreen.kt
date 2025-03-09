package ie.setu.incident_tracker.ui.device

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination

object AddDeviceDestination : NavigationDestination {
    override val route = "signUp"
    override val titleRes = R.string.SignUp_screen
    const val IncidentIDArg = "incidentID"
    val routeWithArgs = "$route/{$IncidentIDArg}"
}

@Composable
fun AddDeviceScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDeviceViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
}