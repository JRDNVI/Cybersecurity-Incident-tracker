package ie.setu.incident_tracker.data

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.device.OfflineDeviceRepository
import ie.setu.incident_tracker.data.firebase.auth.AuthRepository
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.incident.OfflineIncidentRepository
import ie.setu.incident_tracker.data.user.OfflineUserRepository
import ie.setu.incident_tracker.data.user.UserRepository

interface AppContainer {
    val incidentRepository : IncidentRepository
    val deviceRepository : DeviceRepository
    val userRepository : UserRepository
    val authRepository: AuthService
    val fireStoreRepository: FireStoreService
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val firestore = FirebaseFirestore.getInstance()

    override val incidentRepository: IncidentRepository by lazy {
        OfflineIncidentRepository(IncidentDatabase.getDatabase(context).incidentDao())
    }

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(IncidentDatabase.getDatabase(context).deviceDao())
    }

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(IncidentDatabase.getDatabase(context).userDao())
    }

    override val authRepository: AuthService by lazy {
        AuthRepository(FirebaseAuth.getInstance())
    }

    override val fireStoreRepository: FireStoreService by lazy {
        FireStoreRepository(firestore)
    }
}