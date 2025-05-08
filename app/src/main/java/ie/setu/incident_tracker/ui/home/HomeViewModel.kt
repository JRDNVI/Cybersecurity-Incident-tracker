package ie.setu.incident_tracker.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val incidentRepository: IncidentRepository,
    private val authService: AuthService,
    private val fireStoreRepository : FireStoreService
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    val currentUser: FirebaseUser?
        get() = authService.currentUser

    init {
        viewModelScope.launch {
            try {
                fireStoreRepository.getAll(currentUser?.email ?: "").collect { incidents ->
                    Log.d("FirestoreFetch", "Fetched Firestore incidents: ${incidents.size}")
                    _homeUiState.value = _homeUiState.value.copy(firestoreIncidentList = incidents)

                    incidents.forEach {
                        Log.d("FirestoreFetch", "Incident: ${it.title} (${it.email})")
                    }
                }
            } catch (e: Exception) {
                Log.e("FirestoreFetch", "Error fetching from Firestore", e)
            }
            currentUser?.let {
                name.value = it.displayName.orEmpty()
                email.value = it.email.orEmpty()
            }
        }
    }



    fun isAuthenticated() = authService.isUserAuthenticatedInFirebase

    fun deleteIncident(incident: Incident, incidentModel: IncidentModel) {
        viewModelScope.launch {
            incidentRepository.deleteItem(incident)
            fireStoreRepository.delete(currentUser?.email!!, incidentModel._id)
        }
    }

    fun updateSearchBox(newFilter: String) {
        _homeUiState.value = _homeUiState.value.copy(filterByIncidentTitle = newFilter)
    }
}

data class HomeUiState(
    val incidentList: List<Incident> = listOf(),
    val firestoreIncidentList: List<IncidentModel> = listOf(),
    val filterByIncidentTitle: String = ""
)

fun IncidentModel.toIncident(): Incident {
    return Incident(
        title = title,
        description = description,
        type = type,
        dateOfOccurrence = dateOfOccurrence,
        location = location,
        longitude = longitude,
        latitude = latitude,
        status = status,
        email = email,
    )
}
