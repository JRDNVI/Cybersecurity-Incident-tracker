package ie.setu.incident_tracker.data.firebase.services

import android.net.Uri
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import kotlinx.coroutines.flow.Flow

typealias IncidentModel = IncidentFireStore
typealias Incidents = Flow<List<IncidentModel>>

interface FireStoreService {
    suspend fun getAll(email: String): Incidents
    suspend fun get(email: String, incidentId: String): IncidentModel?
    suspend fun insert(email: String, incident: IncidentModel): String
    suspend fun update(email: String, incident: IncidentModel)
    suspend fun delete(email: String, incidentId: String)
    suspend fun addDeviceToIncident(incidentId: String, device: DeviceFireStore)
    suspend fun deleteDeviceFromIncident(incidentId: String, deviceId: String)
    suspend fun updateDeviceInIncident(incidentId: String, updatedDevice: DeviceFireStore)
    suspend fun updatePhotoUris(email: String, uri: Uri)
}