package ie.setu.incident_tracker.data

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.device.OfflineDeviceRepository
import ie.setu.incident_tracker.data.firebase.auth.AuthRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.incident.OfflineIncidentRepository
import ie.setu.incident_tracker.data.user.OfflineUserRepository
import ie.setu.incident_tracker.data.user.UserRepository

interface AppContainer {
    val incidentRepository : IncidentRepository
    val deviceRepository : DeviceRepository
    val userRepository : UserRepository
    val authRepository: AuthRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val incidentRepository: IncidentRepository by lazy {
        OfflineIncidentRepository(IncidentDatabase.getDatabase(context).incidentDao())
    }

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(IncidentDatabase.getDatabase(context).deviceDao())
    }

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(IncidentDatabase.getDatabase(context).userDao())
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepository(FirebaseAuth.getInstance())
    }
}