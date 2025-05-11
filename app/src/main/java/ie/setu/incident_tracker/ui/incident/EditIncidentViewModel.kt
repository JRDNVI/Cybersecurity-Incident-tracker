package ie.setu.incident_tracker.ui.incident

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditIncidentViewModel(
    savedStateHandle: SavedStateHandle,
    private val incidentRepository: IncidentRepository,
    private val authRepository: AuthService,
    private val fireStoreRepository: FireStoreService
) : ViewModel() {

    var incidentUiState by mutableStateOf(IncidentUiState())
        private set

    private val incidentID: String = checkNotNull(savedStateHandle["incidentID"])
    private val userEmail = authRepository.email!!
    private var fireStoreDocumentId: String? = null

    init {
        viewModelScope.launch {
            fireStoreRepository.get(userEmail, incidentID)?.let { incidentFS ->
                fireStoreDocumentId = incidentFS._id


                incidentUiState = IncidentUiState(
                    incidentDetails = incidentFS.toIncidentDetails(),
                    isEntryValid = true
                )
            }
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

    suspend fun updateIncident() : Boolean {
        if (!validateInput(incidentUiState.incidentDetails)) return false
        try {
            val updatedLocal = incidentUiState.incidentDetails.toItem().copy(email = userEmail)
            incidentRepository.updateItem(updatedLocal)

            val firestoreIncident = updatedLocal.toFireStoreModel().copy(
                _id = fireStoreDocumentId ?: ""
            )
            fireStoreRepository.update(userEmail, firestoreIncident)
            return true
        } catch (e : Exception) {
            Log.d("Update Error", e.toString())
        }
        return false
    }
}

fun IncidentFireStore.toIncidentDetails(): IncidentDetails {
    return IncidentDetails(
        title = this.title,
        description = this.description,
        type = this.type,
        dateOfOccurrence = this.dateOfOccurrence,
        location = this.location,
        longitude = this.longitude.toString(),
        latitude = this.latitude.toString(),
        status = this.status,
        email = this.email,
        imageUri = this.imageUri.toUri()
    )
}
