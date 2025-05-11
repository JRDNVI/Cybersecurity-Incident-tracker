package ie.setu.incident_tracker.ui.incident

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
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
    navigateToAddDevice: (String) -> Unit,
    navigateToEditDevice: (String, String) -> Unit,
    navigateToProfile: () -> Unit,
    navigateToMap: () -> Unit,
    navigateToCveDetail: (String) -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewIncidentDetailsViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val viewIncidentUiState = viewModel.incidentDetailsUiState.collectAsState()
    val scope = rememberCoroutineScope()
    Log.d("ViewIncidentDetailsScreen", "Incident ID: $viewIncidentUiState")

    LaunchedEffect(Unit) {
        viewModel.refreshIncident()
    }


    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(ViewIncidentDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                onToggleDarkMode = { onToggleDarkMode() }
            )
        },

        bottomBar = {
            IncidentTrackerBottomBar(
                currentDestination = "",
                navigateToHome = navigateToHome,
                navigateToProfile = { navigateToProfile()},
                navigateToMap = { navigateToMap() },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddDevice(viewIncidentUiState.value.incidentID) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Incident")
            }
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
                navigateToCveDetail = navigateToCveDetail,
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
    selectedDevice: (String, String) -> Unit,
    scope: CoroutineScope,
    navigateToCveDetail: (String) -> Unit,
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
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
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
                        text = incidentDetailsUiState.incidentDetails.dateOfOccurrence,
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
                    text = "Attack Type: ${incidentDetailsUiState.incidentDetails.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Location: ${incidentDetailsUiState.incidentDetails.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Status: ${if (incidentDetailsUiState.incidentDetails.status) "Open" else "Closed"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row {
                    Text(
                        text = "Longitude: ${incidentDetailsUiState.incidentDetails.longitude}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Latitude: ${incidentDetailsUiState.incidentDetails.latitude}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            ListDevices(
                deviceList = incidentDetailsUiState.deviceList,
                selectedDevice = { deviceId ->
                    selectedDevice(incidentDetailsUiState.incidentID, deviceId)
                },
                onFilterTextChange = { viewModel.updateFilterText(it) },
                filterText = incidentDetailsUiState.filterText,
                scope = scope,
                navigateToCveDetail = navigateToCveDetail,
                viewModel = viewModel,
                modifier = Modifier.padding(16.dp)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDevices(
    deviceList: List<DeviceFireStore>,
    selectedDevice: (String) -> Unit,
    onFilterTextChange: (String) -> Unit,
    filterText: String,
    scope: CoroutineScope,
    navigateToCveDetail: (String) -> Unit,
    viewModel: ViewIncidentDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val affectedDevices = remember { mutableIntStateOf(0) }
    affectedDevices.intValue = deviceList.size

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Affected Devices: ${affectedDevices.intValue}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(8.dp)
        )

    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = filterText ,
            onValueChange = onFilterTextChange,
            label = { Text("Filter Devices") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        val filteredDevices = deviceList.filter { it.name.contains(filterText, ignoreCase = true) }
        items(filteredDevices) { device ->
            DeviceCard(
                device = device,
                selectedDevice = selectedDevice,
                scope = scope,
                navigateToCveDetail = navigateToCveDetail,
                modifier = modifier,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun DeviceCard(
    device: DeviceFireStore,
    selectedDevice: (String) -> Unit,
    scope: CoroutineScope,
    modifier: Modifier = Modifier,
    viewModel: ViewIncidentDetailsViewModel,
    navigateToCveDetail: (String) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
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
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "CVE: ${device.cveNumber}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clickable {
                            if (device.cveNumber.isNotBlank()) {
                                navigateToCveDetail(device.cveNumber)
                            }
                        }
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
                           viewModel.deleteDevice(device.deviceID)

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