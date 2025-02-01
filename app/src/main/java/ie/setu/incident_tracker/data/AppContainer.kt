package ie.setu.incident_tracker.data

import android.content.Context
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.device.OfflineDeviceRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.incident.OfflineIncidentRepository

interface AppContainer {
    val incidentRepository : IncidentRepository
    val deviceRepository : DeviceRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val incidentRepository: IncidentRepository by lazy {
        OfflineIncidentRepository(IncidentDatabase.getDatabase(context).incidentDao())
    }

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(IncidentDatabase.getDatabase(context).deviceDao())
    }
}