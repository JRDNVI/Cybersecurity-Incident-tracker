package ie.setu.incident_tracker.ui.incident

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditIncidentViewModel(
    savedStateHandle: SavedStateHandle,
    private val incidentRepository: IncidentRepository
) : ViewModel() {

    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val incidentID: Int = checkNotNull(savedStateHandle["incidentID"])

    init {
        viewModelScope.launch {
            incidentUiState = incidentRepository.getItemStream(incidentID)
                .filterNotNull()
                .first()
                .toIncidentUiState(true)
        }
    }

    private fun validateInput(uiState: IncidentDetails = incidentUiState.incidentDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && description.isNotBlank() && location.isNotBlank()
        }
    }

    fun updateUiState(incidentDetails: IncidentDetails) {
        incidentUiState =
            IncidentUiState(
                incidentDetails = incidentDetails,
                isEntryValid = validateInput(incidentDetails)
            )
    }

    suspend fun updateIncident() {
        if (validateInput(incidentUiState.incidentDetails)) {
            incidentRepository.updateItem(incidentUiState.incidentDetails.toItem())
        }
    }
}