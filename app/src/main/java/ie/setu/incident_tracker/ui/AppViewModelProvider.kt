package ie.setu.incident_tracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import ie.setu.incident_tracker.IncidentTrackerApplication
import ie.setu.incident_tracker.ui.home.HomeViewModel
import ie.setu.incident_tracker.ui.incident.AddIncidentViewModel
import ie.setu.incident_tracker.ui.device.AddDeviceViewModel
import ie.setu.incident_tracker.ui.incident.ViewIncidentDetailsViewModel
import ie.setu.incident_tracker.ui.device.EditDeviceViewModel
import ie.setu.incident_tracker.ui.incident.EditIncidentViewModel
import ie.setu.incident_tracker.ui.auth.login.LoginViewModel
import ie.setu.incident_tracker.ui.auth.register.RegisterViewModel
import ie.setu.incident_tracker.ui.cve.CveDetailsViewModel
import ie.setu.incident_tracker.ui.map.MapViewModel
import ie.setu.incident_tracker.ui.profile.ProfileViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {

        initializer {
            val application = this.IncidentTrackerApplication()
            HomeViewModel(
                application.container.incidentRepository,
                application.container.authRepository,
                application.container.fireStoreRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            LoginViewModel(
                application.container.authRepository,
                application.container.credentialManager,
                application.container.credentialRequest
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            RegisterViewModel(
                auth = FirebaseAuth.getInstance(),
                authService = application.container.authRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            AddIncidentViewModel(
                application.container.incidentRepository,
                application.container.authRepository,
                application.container.fireStoreRepository,
                application.container.locationRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            AddDeviceViewModel(
                this.createSavedStateHandle(),
                application.container.deviceRepository,
                application.container.authRepository,
                application.container.fireStoreRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            ViewIncidentDetailsViewModel(
                this.createSavedStateHandle(),
                application.container.fireStoreRepository,
                application.container.authRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                application.container.fireStoreRepository,
                application.container.authRepository
            )
        }

        initializer {
            val application = this.IncidentTrackerApplication()
            EditIncidentViewModel(
                this.createSavedStateHandle(),
                application.container.incidentRepository,
                application.container.authRepository,
                application.container.fireStoreRepository
            )
        }
        initializer {
            val application = this.IncidentTrackerApplication()
            ProfileViewModel(
                application.container.authRepository,
                application.container.fireStoreRepository
            )
        }
        initializer {
            val application = this.IncidentTrackerApplication()
            CveDetailsViewModel(
                this.createSavedStateHandle(),
                application.container.cveRepository
            )
        }
        initializer {
            val application = this.IncidentTrackerApplication()
            MapViewModel(
                application.container.locationRepository,
                application.container.fireStoreRepository
            )
        }
    }
}

fun CreationExtras.IncidentTrackerApplication(): IncidentTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IncidentTrackerApplication)
