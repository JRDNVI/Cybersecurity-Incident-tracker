package ie.setu.incident_tracker.ui.incident

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope

data object ViewIncidentDetailsDestination : NavigationDestination {
    override val route = "incident_details"
    override val titleRes = R.string.Incident_details_screen
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"

}

@Composable
fun ViewIncidentDetailsScreen(
    navigateBack: () -> Unit,
    navigateToHome:() -> Unit,
    navigateToAddDevice: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewIncidentDetailsViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val viewIncidentUiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    Log.d("ViewIncidentDetailsScreen", "Incident ID: $viewIncidentUiState")

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(ViewIncidentDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = navigateToHome,
                additionalIcons = listOf(
                    Icons.Default.Add to { navigateToAddDevice(viewIncidentUiState.value.incidentDetails.incidentID) }
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            IncidentDetailsBody(
                incidentDetailsUiState = viewIncidentUiState.value,
                selectedDevice = navigateToAddDevice,
                scope = scope,
                viewModel = viewModel,
                modifier = modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun IncidentDetailsBody(
    incidentDetailsUiState: IncidentDetailsUiState,
    selectedDevice: (Int) -> Unit,
    scope: CoroutineScope,
    viewModel: ViewIncidentDetailsViewModel,
    modifier: Modifier = Modifier

    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = incidentDetailsUiState.incidentDetails.title,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = incidentDetailsUiState.incidentDetails.dateOfOccurrence + " " + incidentDetailsUiState.incidentDetails.status,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = incidentDetailsUiState.incidentDetails.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Location: ${incidentDetailsUiState.incidentDetails.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            ListDevices(
                deviceList = incidentDetailsUiState.deviceList,
                selectedDevice = selectedDevice,
                scope = scope,
                viewModel = viewModel,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ListDevices(
    deviceList: List<Device>,
    selectedDevice: (Int) -> Unit,
    scope: CoroutineScope,
    viewModel: ViewIncidentDetailsViewModel,
    modifier: Modifier = Modifier
) {

}

@Composable
fun DeviceCard(
    device: Device,
    selectedDevice: (Int) -> Unit,
    scope: CoroutineScope,
    viewModel: ViewIncidentDetailsViewModel
) {

}
