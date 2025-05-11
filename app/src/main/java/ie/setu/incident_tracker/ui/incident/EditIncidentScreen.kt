package ie.setu.incident_tracker.ui.incident

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

data object EditIncidentDestination : NavigationDestination {
    override val route = "edit_incident"
    override val titleRes = R.string.edit_incident_screen
    const val incidentIdArg = "incidentID"
    val routeWithArgs = "$route/{$incidentIdArg}"
}

@Composable
fun EditIncidentScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    navigateToProfile: () -> Unit,
    onToggleDarkMode: () -> Unit,
    navigateToMap: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditIncidentViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = stringResource(EditIncidentDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp,
                onToggleDarkMode = { onToggleDarkMode() }
            )
        },
        bottomBar = {
            IncidentTrackerBottomBar(
                currentDestination = "",
                navigateToHome = navigateBack,
                navigateToProfile = { navigateToProfile() },
                navigateToMap = { navigateToMap() }
            )
        }
    ) { innerPadding ->
        AddIncidentBody(
            incidentUiState = viewModel.incidentUiState,
            onIncidentValueChange = viewModel::updateUiState,
            onSaveClick = {
                scope.launch {
                    if (viewModel.updateIncident()) {
                        Toast.makeText(context, "Update Successful!", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(context, "Not your Incident!", Toast.LENGTH_SHORT).show()
                    }
                    navigateBack()
                }
            },
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}