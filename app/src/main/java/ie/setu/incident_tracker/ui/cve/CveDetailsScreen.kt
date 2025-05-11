package ie.setu.incident_tracker.ui.cve

import CveResponse
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.components.CveDetailCard
import ie.setu.incident_tracker.ui.device.AddDeviceDestination
import ie.setu.incident_tracker.ui.navigation.NavigationDestination


object cveDetailsDestination : NavigationDestination {
    override val route = "cveDetails"
    override val titleRes = R.string.cve_details
    const val cveIDArg = "cveId"
    val routeWithArgs = "$route/{$cveIDArg}"
}

@Composable
fun CveDetailsScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateBack: () -> Unit,
    onToggleDarkMode: () -> Unit,
    viewModel: CveDetailsViewModel = viewModel(factory = AppViewModelProvider.factory),
) {
    var cveResponse by remember { mutableStateOf<CveResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        try {
            cveResponse = viewModel.getDetails()
        } catch (e: Exception) {
            errorMessage = "Failed to load CVE details: ${e.message}"
        } finally {
            isLoading = false
        }
    }

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
                navigateToProfile = { navigateToProfile() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Text(text = errorMessage ?: "Unknown error", modifier = modifier.padding(16.dp))
                }
                cveResponse != null -> {
                    CveDetailCard(cveResponse = cveResponse!!, modifier = modifier.padding(16.dp))
                }
                else -> {
                    Text("No CVE data found.", modifier = modifier.padding(16.dp))
                }
            }
        }
    }
}





