package ie.setu.incident_tracker.ui.map

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination

object MapScreenDestination : NavigationDestination{
    override val route = "map"
    override val titleRes = R.string.map_screen
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navigateToProfile: () -> Unit,
    onToggleDarkMode: () -> Unit,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    androidx.compose.material3.Scaffold(
        topBar = {
            IncidentTrackerTopAppBar(
                title = "Map",
                canNavigateBack = true,
                modifier =  Modifier,
                navigateUp = navigateBack,
                onToggleDarkMode = { onToggleDarkMode() },
                onToggleListAll = { }
            )

        },
        bottomBar = {
            IncidentTrackerBottomBar(
                currentDestination = "Map",
                navigateToHome = { navigateToHome() },
                navigateToProfile = { navigateToProfile() },
                navigateToMap = {  }
            )
        }
    ) { innerPadding ->

        Box(Modifier.padding(innerPadding)) {
            val locationPermissions = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            LaunchedEffect(locationPermissions.allPermissionsGranted) {
                locationPermissions.launchMultiplePermissionRequest()
                if (locationPermissions.allPermissionsGranted) {
                    viewModel.getLocationUpdates()
                }
            }

            val uiSettings by remember {
                mutableStateOf(
                    MapUiSettings(
                        myLocationButtonEnabled = locationPermissions.allPermissionsGranted,
                        compassEnabled = true,
                        mapToolbarEnabled = true
                    )
                )
            }
            val properties by remember {
                mutableStateOf(
                    MapProperties(
                        mapType = MapType.NORMAL,
                        isMyLocationEnabled = locationPermissions.allPermissionsGranted,
                    )
                )
            }
            val currentLocation = viewModel.currentLatLng.collectAsState().value
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(currentLocation, 14f)
            }

            if (locationPermissions.allPermissionsGranted)
                LaunchedEffect(currentLocation) {
                    viewModel.getLocationUpdates()
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(currentLocation))
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(
                        currentLocation, 14f
                    )
                }


            val incidents = viewModel.incidents.collectAsState().value.firestoreIncidentList
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    properties = properties
                ) {
                    incidents.forEach {
                        val position = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                        Marker(
                            state = MarkerState(position = position),
                            title = it.title,
                            snippet = it.type
                        )
                    }
                }
            }
        }
    }
}

