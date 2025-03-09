package ie.setu.incident_tracker.ui.incident

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ViewIncidentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val incidentRepository: IncidentRepository,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val incidentID: Int = checkNotNull(savedStateHandle[ViewIncidentDetailsDestination.incidentIdArg])


    val uiState: StateFlow<IncidentDetailsUiState> =
        combine(
            incidentRepository.getItemStream(incidentID),
            deviceRepository.getAllItemsStream()
        ) { incident, devices ->
            if (incident != null) {
                val deviceList = devices.filter { it.incidentID == incidentID }

                IncidentDetailsUiState(
                    incidentDetails = incident.toIncidentDetails(),
                    deviceList = deviceList
                )
            } else {
                IncidentDetailsUiState()
            }

        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = IncidentDetailsUiState()
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class IncidentDetailsUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val deviceList: List<Device> = listOf()
)