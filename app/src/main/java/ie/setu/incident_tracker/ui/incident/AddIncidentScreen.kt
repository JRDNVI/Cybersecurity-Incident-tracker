package ie.setu.incident_tracker.ui.incident

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AddIncidentDestination : NavigationDestination {
    override val route = "add_incident_screen"
    override val titleRes = R.string.Add_incident_screen
}

@Composable
fun AddIncidentScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateHome: () -> Unit,
    navigateToProfile: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddIncidentViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(AddIncidentDestination.titleRes),
                canNavigateBack = true,
                modifier = modifier,
                navigateUp = onNavigateUp,
                onToggleDarkMode = { onToggleDarkMode() }

            )
        },

        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = navigateHome,
                navigateToProfile = { navigateToProfile() }
            )
        }
    ) { innerPadding ->
        AddIncidentBody(
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        )
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun AddIncidentBody(
    incidentUiState: IncidentUiState,
    onIncidentValueChange: (IncidentDetails) -> Unit,
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
                value = incidentUiState.incidentDetails.title,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(title = it)) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            CyberAttackDropdown(
                selectedType = incidentUiState.incidentDetails.type,
                onTypeSelected = { newType ->
                    onIncidentValueChange(incidentUiState.incidentDetails.copy(type = newType))
                }
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.description,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(description = it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = if (incidentUiState.incidentDetails.dateOfOccurrence == "") {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                } else incidentUiState.incidentDetails.dateOfOccurrence,

                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(dateOfOccurrence = it)) },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.location,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(location = it)) },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.longitude,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(longitude = it)) },
                label = { Text("Longitude") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.latitude,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(latitude = it)) },
                label = { Text("Latitude") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = incidentUiState.incidentDetails.status,
                    onCheckedChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(status = it)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Is this incident resolved?")
            }
        }
        item {
            Button(
                onClick = onSaveClick,
                enabled = incidentUiState.isEntryValid,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Text("Add Incident")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberAttackDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val attackTypes = listOf(
        "Phishing", "Malware", "Ransomware", "DDoS", "SQL Injection",
        "Man-in-the-Middle", "Zero-Day Exploit", "Trojan Horse", "XSS", "Brute Force Attack"
    )

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type") },
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
            attackTypes.forEach { attackType ->
                DropdownMenuItem(
                    text = { Text(attackType) },
                    onClick = {
                        onTypeSelected(attackType)
                        expanded = false
                    }
                )
            }
        }
    }
}