package ie.setu.incident_tracker.ui.device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object EditDeviceDestination : NavigationDestination {
    override val route = "edit_device"
    override val titleRes = R.string.edit_device_screen
    const val deivceIdArg = "deviceID"
    const val incidentIdArg = "IncidentID"
    val routeWithArgs = "$route/{$incidentIdArg}/{$deivceIdArg}"

}

@Composable
fun EditDeviceScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDeviceViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val deviceUiState = viewModel.deviceUiState
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(EditDeviceDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = { navigateBack() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AddDeviceScreen(
                deviceUiState = deviceUiState,
                onDeviceValueChange = viewModel::updateUiState,
                onSaveClick = {
                    scope.launch {
                        viewModel.updateItem()
                        onNavigateUp()
                    }
                }
            )
        }
    }
}
