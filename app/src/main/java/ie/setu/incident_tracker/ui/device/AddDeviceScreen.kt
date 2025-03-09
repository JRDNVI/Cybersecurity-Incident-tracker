package ie.setu.incident_tracker.ui.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object AddDeviceDestination : NavigationDestination {
    override val route = "signUp"
    override val titleRes = R.string.SignUp_screen
    const val IncidentIDArg = "incidentID"
    val routeWithArgs = "$route/{$IncidentIDArg}"
}

@Composable
fun AddDeviceScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDeviceViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(AddDeviceDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = { navigateToHome() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            AddDeviceScreen(
                deviceDetails = viewModel.deviceUiState.deviceDetails,
                onDeviceValueChange = viewModel::updateUiState,
                onSaveClick = {
                    scope.launch {
                        viewModel.saveItem()
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun AddDeviceScreen(
    deviceDetails: DeviceDetails,
    onDeviceValueChange: (DeviceDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            OutlinedTextField(
                value = deviceDetails.name,
                onValueChange = { onDeviceValueChange(deviceDetails.copy(name = it)) },
                label = { Text("Device Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = deviceDetails.ipAddress,
                onValueChange = { onDeviceValueChange(deviceDetails.copy(ipAddress = it)) },
                label = { Text("IP Address") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = deviceDetails.macAddress,
                onValueChange = { onDeviceValueChange(deviceDetails.copy(macAddress = it)) },
                label = { Text("MAC Address") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = deviceDetails.operatingSystem,
                onValueChange = { onDeviceValueChange(deviceDetails.copy(operatingSystem = it)) },
                label = { Text("Operating System") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = deviceDetails.cveNumber,
                onValueChange = { onDeviceValueChange(deviceDetails.copy(cveNumber = it)) },
                label = { Text("CVE Number") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save Device")
            }
        }
    }
}