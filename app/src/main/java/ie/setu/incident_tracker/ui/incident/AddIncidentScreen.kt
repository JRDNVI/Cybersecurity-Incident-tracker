package ie.setu.incident_tracker.ui.incident

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

object AddIncidentDestination : NavigationDestination {
    override val route = "add_incident_screen"
    override val titleRes = R.string.Add_incident_screen
}

@Composable
fun AddIncidentScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateHome: () -> Unit,
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

            )
        },

        bottomBar = {
            IncidentTrackerBottomBar(navigateToHome = navigateHome)
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
            OutlinedTextField(
                value = incidentUiState.incidentDetails.description,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(description = it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.type,
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(type = it)) },
                label = { Text("type") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.dateOfOccurrence,
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
                value = incidentUiState.incidentDetails.longitude.toString(),
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(longitude = it.toFloat())) },
                label = { Text("Longitude") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.latitude.toString(),
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(latitude = it.toFloat())) },
                label = { Text("Latitude") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = incidentUiState.incidentDetails.status.toString(),
                onValueChange = { onIncidentValueChange(incidentUiState.incidentDetails.copy(status = it.toBoolean())) },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth()
            )
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