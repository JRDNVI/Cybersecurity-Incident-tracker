package ie.setu.incident_tracker.ui.incident

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _filterText = MutableStateFlow("")

    private val _uiState: StateFlow<IncidentDetailsUiState> =
        combine(
            incidentRepository.getItemStream(incidentID),
            deviceRepository.getAllItemsStream(),
            _filterText
        ) { incident, devices, filterText ->
            if (incident != null) {
                val deviceList = devices.filter { it.incidentID == incidentID }
                IncidentDetailsUiState(
                    incidentDetails = incident.toIncidentDetails(),
                    deviceList = deviceList,
                    filterText = filterText
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

    val incidentDetailsUiState: StateFlow<IncidentDetailsUiState> = _uiState

    suspend fun deleteDevice(device: Device) {
        deviceRepository.deleteItem(device)
    }

    fun updateFilterText(newFilter: String) {
        _filterText.value = newFilter
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class IncidentDetailsUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val deviceList: List<Device> = listOf(),
    val filterText: String = ""
)