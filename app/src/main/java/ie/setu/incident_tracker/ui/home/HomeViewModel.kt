package ie.setu.incident_tracker.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val incidentRepository: IncidentRepository,
    private val authService: AuthService,
    private val fireStoreRepository : FireStoreService
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()
    private var showingAll by mutableStateOf(false)

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    val currentUser: FirebaseUser?
        get() = authService.currentUser

    init {
        loadUserIncidents()
    }

    fun loadUserIncidents() {
        showingAll = false
        currentUser?.let { user ->
            viewModelScope.launch {
                fireStoreRepository.getAll(user.email ?: "").collect { incidents ->
                    _homeUiState.value = _homeUiState.value.copy(firestoreIncidentList = incidents)
                }
            }
        }
    }

    suspend fun listAllIncidents() {
        showingAll = true
        val allIncidents = fireStoreRepository.getAllIncidents().first()
        _homeUiState.update { it.copy(firestoreIncidentList = allIncidents) }
        Log.d("List all", allIncidents.toString())
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
