package ie.setu.incident_tracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ie.setu.incident_tracker.IncidentTrackerApplication
import ie.setu.incident_tracker.ui.home.HomeViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            HomeViewModel(
                IncidentTrackerApplication().container.incidentRepository
            )
        }
    }
}

fun CreationExtras.IncidentTrackerApplication() : IncidentTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IncidentTrackerApplication)