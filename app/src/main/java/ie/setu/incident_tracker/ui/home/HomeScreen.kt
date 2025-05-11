package ie.setu.incident_tracker.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import ie.setu.incident_tracker.ui.components.SwipeableIncidentCard
import androidx.compose.material3.Icon as M3Icon
import androidx.compose.material3.TextField as M3TextField
import androidx.compose.material3.DropdownMenu as M3DropdownMenu
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
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var expand by remember { mutableStateOf(false) }
    val currentUser = viewModel.currentUser
    val userName = if (viewModel.isAuthenticated()) currentUser?.displayName else ""

    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = "Welcome, ${userName ?: "User"}",
                canNavigateBack = false,
                actions = {
                    IconButton(onClick = { expand = true }) {
                        M3Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(24.dp))
                    }
                    M3DropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                        DropdownMenuItem(text = { M3Text("Analytics", fontSize = 16.sp) }, onClick = { expand = false })
                        DropdownMenuItem(text = { M3Text("Profile", fontSize = 16.sp) }, onClick = { expand = false })
                        DropdownMenuItem(text = { M3Text("Logout", fontSize = 16.sp) }, onClick = {
                            expand = false
                            navigateToSignInScreen()
                        })
                    }
                }
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                navigateToHome = {},
                navigateToProfile = { navigateToProfile() },
                additionalIcons = listOf(Icons.Default.Add to { navigateToAddIncident() })
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
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        M3TextField(
            value = filterText,
            onValueChange = onFilterTextChange,
            label = { M3Text("Search") },
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            shape = RoundedCornerShape(8.dp)
        )
        if (incidentList.isEmpty()) {
            M3Text(
                "No Incidents",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            val filteredIncidents = incidentList.filter {
                it.title.contains(filterText, ignoreCase = true)
            }

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

