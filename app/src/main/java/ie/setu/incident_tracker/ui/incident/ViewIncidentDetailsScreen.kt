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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch

data object ViewIncidentDetailsDestination : NavigationDestination {
    override val route = "incident_details"
    override val titleRes = R.string.Incident_details_screen
    const val incidentIdArg = "incidentId"
    val routeWithArgs = "$route/{$incidentIdArg}"

}

@Composable
fun ViewIncidentDetailsScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToAddDevice: (Int) -> Unit,
    navigateToEditDevice: (Int) -> Unit,
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
                selectedDevice = navigateToEditDevice,
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
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(deviceList) { device ->
            DeviceCard(
                device = device,
                selectedDevice = selectedDevice,
                scope = scope,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun DeviceCard(
    device: Device,
    selectedDevice: (Int) -> Unit,
    scope: CoroutineScope,
    viewModel: ViewIncidentDetailsViewModel
) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Device Name: ${device.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "IP Address: ${device.ipAddress}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "MAC Address: ${device.macAddress}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "OS: ${device.operatingSystem}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = "CVE: ${device.cveNumber}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row {
                IconButton(onClick = { selectedDevice(device.deviceID) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_device_icon),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { showDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_device_icon),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Device") },
            text = { Text("Are you sure you want to delete this device?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        scope.launch {
                            viewModel.deleteDevice(device)
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}