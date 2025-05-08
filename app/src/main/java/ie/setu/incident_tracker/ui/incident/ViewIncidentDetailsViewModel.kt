package ie.setu.incident_tracker.ui.incident

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.firebase.auth.AuthRepository
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.ui.home.toIncident
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewIncidentDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val fireStoreRepository: FireStoreService,
    private val authRepository: AuthService
) : ViewModel() {

    private val incidentID: String = checkNotNull(savedStateHandle[ViewIncidentDetailsDestination.incidentIdArg])
    private val userEmail = authRepository.email!!

    private val _filterText = MutableStateFlow("")
    private val _uiState = MutableStateFlow(IncidentDetailsUiState())
    val incidentDetailsUiState: StateFlow<IncidentDetailsUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val firestoreIncident = fireStoreRepository.get(userEmail, incidentID)
                firestoreIncident?.let {
                    _uiState.value = IncidentDetailsUiState(
                        incidentDetails = it.toIncident().toIncidentDetails(),
                        deviceList = it.devices, // <- from Firestore embedded list
                        filterText = _filterText.value
                    )
                }
            } catch (e: Exception) {
                Log.e("ViewIncidentDetails", "Error fetching Firestore incident", e)
            }
        }
    }

    fun updateFilterText(newFilter: String) {
        _filterText.value = newFilter
        _uiState.value = _uiState.value.copy(
            filterText = newFilter,
            deviceList = _uiState.value.deviceList.filter {
                it.name.contains(newFilter, ignoreCase = true)
            }
        )
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class IncidentDetailsUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val deviceList: List<DeviceFireStore> = listOf(),
    val filterText: String = ""
)



