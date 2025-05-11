package ie.setu.incident_tracker.ui.incident

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import ie.setu.incident_tracker.data.firebase.auth.AuthRepository
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.location.LocationService
import kotlinx.coroutines.launch

class AddIncidentViewModel(
    private val incidentRepository: IncidentRepository,
    private val authRepository: AuthService,
    private val fireStoreRepository: FireStoreService,
    private val locationService: LocationService
) : ViewModel() {

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    fun startCollectingLocation() {
        viewModelScope.launch {
            locationService.getLocationFlow().collect { location ->
                if (incidentUiState.useCurrentLocation && location != null) {
                    val lat = location.latitude.toString()
                    val long = location.longitude.toString()

                    incidentUiState = incidentUiState.copy(
                        incidentDetails = incidentUiState.incidentDetails.copy(
                            latitude = lat,
                            longitude = long
                        )
                    )
                }
            }
        }
    }

    fun toggleUseCurrentLocation(enabled: Boolean) {
        incidentUiState = incidentUiState.copy(useCurrentLocation = enabled)

        if (enabled) {
            startCollectingLocation()
        }
    }

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState = incidentUiState.copy(
            incidentDetails = incidentDetails,
            isEntryValid = validateInput(incidentDetails)
        )
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && type.isNotBlank()
        }
    }

    suspend fun saveItem() {
        if (validateInput()) {
            val userEmail = currentUser?.email ?: return
            val updatedDetails = incidentUiState.incidentDetails.copy(email = userEmail)
            val localIncident = updatedDetails.toItem()

            fireStoreRepository.insert(userEmail, localIncident.toFireStoreModel())
            incidentRepository.insertItem(localIncident)
        }
    }
}

data class IncidentUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val isEntryValid: Boolean = false,
    val useCurrentLocation: Boolean = true
)

data class IncidentDetails(
    val incidentID: Int = 0,
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val dateOfOccurrence: String = "",
    val location: String = " ",
    val longitude: String = "0",
    val latitude: String = "0",
    val email: String = "0",
    val status: Boolean = false,
    val imageUri: Uri = Uri.EMPTY
)

fun IncidentDetails.toItem(): Incident = Incident(
    incidentID = incidentID,
    title = title,
    description = description,
    type = type,
    dateOfOccurrence = dateOfOccurrence,
    location = location,
    longitude = longitude.toFloat(),
    latitude = latitude.toFloat(),
    email = email,
    status = status,
    imageUri = imageUri
)

fun Incident.toIncidentUiState(isEntryValid: Boolean = false): IncidentUiState = IncidentUiState(
    incidentDetails = this.toIncidentDetails(),
    isEntryValid = isEntryValid
)

fun Incident.toIncidentDetails(): IncidentDetails = IncidentDetails(
    incidentID = incidentID,
    title = title,
    description = description,
    type = type,
    dateOfOccurrence = dateOfOccurrence,
    location = location,
    longitude = longitude.toString(),
    latitude = latitude.toString(),
    email = email,
    status = status
)

fun Incident.toFireStoreModel(): IncidentFireStore {
    return IncidentFireStore(
        title = title,
        description = description,
        type = type,
        dateOfOccurrence = dateOfOccurrence,
        location = location,
        longitude = longitude,
        latitude = latitude,
        status = status,
        email = email,
        imageUri = imageUri.toString()

    )
}
