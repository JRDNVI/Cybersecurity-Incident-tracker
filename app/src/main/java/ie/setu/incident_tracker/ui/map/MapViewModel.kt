package ie.setu.incident_tracker.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.location.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val locationRepository: LocationRepository,
    private val fireStoreRepository: FireStoreService
) : ViewModel() {

    private val _incidents = MutableStateFlow(MapUiState())
    val incidents: StateFlow<MapUiState> = _incidents.asStateFlow()

    init {
        viewModelScope.launch {
            val allIncidents = fireStoreRepository.getAllIncidents().first()
            _incidents.update { it.copy(firestoreIncidentList = allIncidents) }
        }
    }

    private val _currentLatLng = MutableStateFlow(LatLng(0.0, 0.0))
    val currentLatLng: StateFlow<LatLng> get() = _currentLatLng

    private fun setCurrentLatLng(latLng: LatLng) {
        _currentLatLng.value = latLng
    }

    fun getLocationUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.getLocationFlow().collect {
                it?.let { location ->
                    setCurrentLatLng(
                        LatLng(location.latitude,
                            location.longitude)
                    )
                }
            }
        }
    }
}

data class MapUiState(
    val firestoreIncidentList: List<IncidentModel> = listOf()
)