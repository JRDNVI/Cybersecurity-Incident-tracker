package ie.setu.incident_tracker.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import ie.setu.incident_tracker.ui.components.SwipeableIncidentCard
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.TextField as M3TextField
import androidx.compose.material3.Text as M3Text


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToAddIncident: () -> Unit,
    navigateToIncidentDetails: (String) -> Unit,
    navigateToEditIncident: (String) -> Unit,
    navigateToSignInScreen: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToMap: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val currentUser = viewModel.currentUser
    var userName = if (viewModel.isAuthenticated()) currentUser?.displayName else ""
    userName = userName?.split(" ")?.firstOrNull() ?: "User"

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = "Welcome, $userName",
                canNavigateBack = false,
                onLogout = { navigateToSignInScreen() },
                onToggleDarkMode = { onToggleDarkMode() },
                onToggleListAll = { enabled ->
                    if (enabled) {
                        viewModel.viewModelScope.launch { viewModel.listAllIncidents() }
                    } else {
                        viewModel.loadUserIncidents()
                    }
                }
            )

        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = {},
                navigateToProfile = { navigateToProfile() },
                navigateToMap = { navigateToMap() },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            HomeBody(
                incidentList = homeUiState.firestoreIncidentList,
                onIncidentSelected = navigateToIncidentDetails,
                onEditIncidentSelected = navigateToEditIncident,
                onDeleteIncidentSelected = { viewModel.deleteIncident(it.toIncident(), it) },
                filterText = homeUiState.filterByIncidentTitle,
                onFilterTextChange = { viewModel.updateSearchBox(it) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun HomeBody(
    incidentList: List<IncidentModel>,
    onIncidentSelected: (String) -> Unit,
    onEditIncidentSelected: (String) -> Unit,
    onDeleteIncidentSelected: (IncidentModel) -> Unit,
    filterText: String,
    onFilterTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchField by remember { mutableStateOf("Title") }
    var sortDescending by remember { mutableStateOf(true) }

    val searchOptions = listOf("Title", "Location", "Type")
    val sortedIncidents = incidentList.sortedBy {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.dateOfOccurrence)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }.let { if (sortDescending) it.reversed() else it }

    val filteredIncidents = sortedIncidents.filter {
        when (searchField) {
            "Title" -> it.title.contains(filterText, ignoreCase = true)
            "Location" -> it.location.contains(filterText, ignoreCase = true)
            "Type" -> it.type.contains(filterText, ignoreCase = true)
            else -> false
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            M3TextField(
                value = filterText,
                onValueChange = onFilterTextChange,
                label = { M3Text("Search") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(8.dp)
            )

            var fieldDropdownExpanded by remember { mutableStateOf(false) }
            Box {
                Button(onClick = { fieldDropdownExpanded = true }) {
                    M3Text(searchField)
                }
                DropdownMenu(
                    expanded = fieldDropdownExpanded,
                    onDismissRequest = { fieldDropdownExpanded = false }
                ) {
                    searchOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { M3Text(option) },
                            onClick = {
                                searchField = option
                                fieldDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { sortDescending = !sortDescending }) {
                M3Text(if (sortDescending) "Newest" else "Oldest")
            }
        }

        if (filteredIncidents.isEmpty()) {
            M3Text(
                "No Incidents",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                items(filteredIncidents, key = { it._id }) { incident ->
                    Spacer(Modifier.padding(5.dp))
                    SwipeableIncidentCard(
                        incident = incident,
                        onClick = { onIncidentSelected(incident._id) },
                        onEdit = { onEditIncidentSelected(incident._id) },
                        onDelete = { onDeleteIncidentSelected(incident) }
                    )
                }
            }
        }
    }
}

