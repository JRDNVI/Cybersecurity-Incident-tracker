package ie.setu.incident_tracker.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
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
    navigateToAddIncidentDetails: (Int) -> Unit,
    navigateToEditIncident: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        bottomBar = {
            IncidentTrackerBottomBar(navigateToHome = {  } )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeBody(
                incidentList = homeUiState.incidentList,
                modifier = Modifier.fillMaxSize()
            )

        }
    }
}

@Composable
fun HomeBody(
    incidentList: List<Incident>,
    modifier: Modifier = Modifier
) {
    Column {
        Text("HEllO")
    }
}