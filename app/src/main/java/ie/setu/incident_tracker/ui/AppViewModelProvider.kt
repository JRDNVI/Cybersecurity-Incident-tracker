package ie.setu.incident_tracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ie.setu.incident_tracker.IncidentTrackerApplication
import ie.setu.incident_tracker.ui.auth.SignInViewModel
import ie.setu.incident_tracker.ui.home.HomeViewModel
import ie.setu.incident_tracker.ui.incident.AddIncidentViewModel
import ie.setu.incident_tracker.ui.auth.SignUpViewModel
import ie.setu.incident_tracker.ui.device.AddDeviceViewModel
import ie.setu.incident_tracker.ui.incident.ViewIncidentDetailsViewModel
import ie.setu.incident_tracker.ui.device.EditDeviceViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {

        initializer {
            HomeViewModel(
                IncidentTrackerApplication().container.incidentRepository
            )
        }

        initializer {
            AddIncidentViewModel(
                IncidentTrackerApplication().container.incidentRepository
            )
        }

        initializer {
            SignInViewModel(
                IncidentTrackerApplication().container.userRepository
            )
        }

        initializer {
            SignUpViewModel(
                IncidentTrackerApplication().container.userRepository
            )
        }

        initializer {
            AddDeviceViewModel(
                this.createSavedStateHandle(),
                IncidentTrackerApplication().container.deviceRepository
            )
        }

        initializer {
            ViewIncidentDetailsViewModel(
                this.createSavedStateHandle(),
                IncidentTrackerApplication().container.incidentRepository,
                IncidentTrackerApplication().container.deviceRepository
            )
        }

        initializer {
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                IncidentTrackerApplication().container.deviceRepository
            )
        }
    }
}

fun CreationExtras.IncidentTrackerApplication() : IncidentTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IncidentTrackerApplication)