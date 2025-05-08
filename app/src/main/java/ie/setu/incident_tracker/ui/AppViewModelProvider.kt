package ie.setu.incident_tracker.ui
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import ie.setu.incident_tracker.IncidentTrackerApplication
import ie.setu.incident_tracker.ui.auth.SignInViewModel
import ie.setu.incident_tracker.ui.home.HomeViewModel
import ie.setu.incident_tracker.ui.incident.AddIncidentViewModel
import ie.setu.incident_tracker.ui.auth.SignUpViewModel
import ie.setu.incident_tracker.ui.device.AddDeviceViewModel
import ie.setu.incident_tracker.ui.incident.ViewIncidentDetailsViewModel
import ie.setu.incident_tracker.ui.device.EditDeviceViewModel
import ie.setu.incident_tracker.ui.incident.EditIncidentViewModel
import ie.setu.incident_tracker.ui.auth.login.LoginViewModel
import ie.setu.incident_tracker.ui.auth.register.RegisterViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {

        initializer {
            HomeViewModel(
                IncidentTrackerApplication().container.incidentRepository,
                IncidentTrackerApplication().container.authRepository,
                IncidentTrackerApplication().container.fireStoreRepository
            )
        }

        initializer {
            LoginViewModel(
                IncidentTrackerApplication().container.authRepository
            )
        }

        initializer {
            RegisterViewModel(
                auth = FirebaseAuth.getInstance(),
                authService = IncidentTrackerApplication().container.authRepository
            )
        }

        initializer {
            AddIncidentViewModel(
                IncidentTrackerApplication().container.incidentRepository,
                IncidentTrackerApplication().container.authRepository,
                IncidentTrackerApplication().container.fireStoreRepository
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
                IncidentTrackerApplication().container.fireStoreRepository,
                IncidentTrackerApplication().container.authRepository

            )
        }

        initializer {
            EditDeviceViewModel(
                this.createSavedStateHandle(),
                IncidentTrackerApplication().container.deviceRepository
            )
        }
        initializer {
            EditIncidentViewModel(
                this.createSavedStateHandle(),
                IncidentTrackerApplication().container.incidentRepository,
                IncidentTrackerApplication().container.authRepository,
                IncidentTrackerApplication().container.fireStoreRepository
            )
        }
    }
}

fun CreationExtras.IncidentTrackerApplication() : IncidentTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IncidentTrackerApplication)