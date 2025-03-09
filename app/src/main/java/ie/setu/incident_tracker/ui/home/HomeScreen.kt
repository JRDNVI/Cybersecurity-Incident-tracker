package ie.setu.incident_tracker.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToAddIncident: () -> Unit,
    navigateToIncidentDetails: (Int) -> Unit,
    navigateToEditIncident: (Int) -> Unit,
    navigateToSignInScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var expand by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                actions = {
                    IconButton(onClick = {expand = true}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expand,
                        onDismissRequest = { expand = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)

                    ) {
                        DropdownMenuItem(
                            text = { Text("Logout", fontSize = 16.sp) },
                            onClick = {
                                expand = false
                                navigateToSignInScreen()
                            }
                        )
                    }
                },
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = {  },
                additionalIcons = listOf(
                    Icons.Default.Add to { navigateToAddIncident()}
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeBody(
                incidentList = homeUiState.incidentList,
                onIncidentSelected = navigateToIncidentDetails,
                onEditIncidentSelected = navigateToEditIncident,
                filterText = homeUiState.filterByIncidentTitle,
                onDeleteIncidentSelected = {viewModel.deleteIncident(it)},
                onFilterTextChange = { viewModel.updateSearchBox(it) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeBody(
    incidentList: List<Incident>,
    onIncidentSelected: (Int) -> Unit,
    onEditIncidentSelected: (Int) -> Unit,
    onDeleteIncidentSelected: (Incident) -> Unit,
    filterText: String,
    onFilterTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TextField(
            value = filterText,
            onValueChange = onFilterTextChange,
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp)
        )
        if (incidentList.isEmpty()) {
            Text(
                text = "No Incidents",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }
        else {
            val filteredIncidents = incidentList.filter { it.title.contains(filterText, ignoreCase = true ) }
            ListIncidents(
                incidentList = filteredIncidents,
                onIncidentSelected = onIncidentSelected,
                onEditIncidentSelected = onEditIncidentSelected,
                onDeleteIncidentSelected = onDeleteIncidentSelected,
                modifier = modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun ListIncidents(
    incidentList: List<Incident>,
    onIncidentSelected: (Int) -> Unit,
    onEditIncidentSelected: (Int) -> Unit,
    onDeleteIncidentSelected: (Incident) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(incidentList, key = { it.incidentID }) { incident ->
            var isExpanded by remember { mutableStateOf(false) }
            var showDeleteDialog by remember { mutableStateOf(false) }

            Card(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = modifier.clickable { onIncidentSelected(incident.incidentID) },
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = incident.title,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = incident.type,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (!incident.status) "Status: Open" else "Status: Closed",
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = { isExpanded = !isExpanded },
                        ) {
                            Text(
                                text = if (isExpanded) "Show Less" else "Show More",
                                textAlign = TextAlign.Right
                            )
                        }
                    }

                    if (isExpanded) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Description: ${incident.description}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Created: ${incident.dateOfOccurrence}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { onEditIncidentSelected(incident.incidentID) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Incident"
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { onDeleteIncidentSelected(incident) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Incident",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}