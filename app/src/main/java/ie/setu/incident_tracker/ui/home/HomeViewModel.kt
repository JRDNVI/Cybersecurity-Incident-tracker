package ie.setu.incident_tracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.incident.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val incidentRepository: IncidentRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            incidentRepository.getAllItemsStream()
                .map { HomeUiState(incidentList = it, filterByIncidentTitle = _homeUiState.value.filterByIncidentTitle) }
                .collect { _homeUiState.value = it }
        }
    }

    fun deleteIncident(incident: Incident) {
        viewModelScope.launch {
            incidentRepository.deleteItem(incident)
        }
    }

    fun updateSearchBox(newFilter: String) {
        _homeUiState.value = _homeUiState.value.copy(filterByIncidentTitle = newFilter)
    }
}

data class HomeUiState(
    val incidentList: List<Incident> = listOf(),
    val filterByIncidentTitle: String = ""
)