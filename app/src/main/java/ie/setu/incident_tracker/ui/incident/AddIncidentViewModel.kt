package ie.setu.incident_tracker.ui.incident

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository

class AddIncidentViewModel(private val incidentRepository: IncidentRepository) : ViewModel() {

    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState = IncidentUiState(incidentDetails = incidentDetails, isEntryValid = validateInput(incidentDetails))
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails) : Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && type.isNotBlank()
        }
    }
    suspend fun saveItem() {
        if (validateInput()) {
            incidentRepository.insertItem(incidentUiState.incidentDetails.toItem())
        }
    }
}

data class IncidentUiState(
    val incidentDetails: IncidentDetails = IncidentDetails(),
    val isEntryValid: Boolean = false
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
    val status: Boolean = false
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
    status = status
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
    status = status
)