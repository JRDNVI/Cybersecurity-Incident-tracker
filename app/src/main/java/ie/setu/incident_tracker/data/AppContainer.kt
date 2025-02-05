package ie.setu.incident_tracker.data

import android.content.Context
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.device.OfflineDeviceRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.incident.OfflineIncidentRepository
import ie.setu.incident_tracker.data.user.OfflineUserRepository
import ie.setu.incident_tracker.data.user.UserRepository

interface AppContainer {
    val incidentRepository : IncidentRepository
    val deviceRepository : DeviceRepository
    val userRepository : UserRepository
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
}