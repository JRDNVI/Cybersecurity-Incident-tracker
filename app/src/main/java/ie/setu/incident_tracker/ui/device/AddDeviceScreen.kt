package ie.setu.incident_tracker.ui.device

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    navigateToProfile: () -> Unit,
    onToggleDarkMode: () -> Unit,
    navigateToMap: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddDeviceViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(AddDeviceDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                onToggleDarkMode = { onToggleDarkMode() }
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = { navigateToHome() },
                navigateToProfile = { navigateToProfile() },
                navigateToMap = { navigateToMap() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            AddDeviceScreen(
                deviceUiState = viewModel.deviceUiState,
                onDeviceValueChange = viewModel::updateUiState,
                onSaveClick = {
                    scope.launch {
                        viewModel.saveItem()
                        navigateBack()
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun AddDeviceScreen(
    deviceUiState: DeviceUiState,
    onDeviceValueChange: (DeviceDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceDetails = deviceUiState.deviceDetails
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
            OperatingSystemDropdown(
                selectedOS = deviceDetails.operatingSystem,
                onTypeSelected = { newOS ->
                    onDeviceValueChange(deviceDetails.copy(operatingSystem = newOS))
                }
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
                enabled = deviceUiState.isEntryValid,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Save Device")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperatingSystemDropdown(
    selectedOS: String,
    onTypeSelected: (String) -> Unit
) {
    val osOptions = listOf("Windows", "macOS", "Linux", "Android", "iOS", "Other")

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOS,
            onValueChange = {},
            readOnly = true,
            label = { Text("Operating System") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon")
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            osOptions.forEach { os ->
                DropdownMenuItem(
                    text = { Text(os) },
                    onClick = {
                        onTypeSelected(os)
                        expanded = false
                    }
                )
            }
        }
    }
}