package ie.setu.incident_tracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(incidentRepository: IncidentRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = incidentRepository.getAllItemsStream()
        .map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState()
        )
}

data class HomeUiState(
    val incidentList: List<Incident> = listOf()
)