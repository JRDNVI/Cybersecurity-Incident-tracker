package ie.setu.incident_tracker.data.firebase.services

import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

typealias IncidentModel = IncidentFireStore
typealias Incidents = Flow<List<IncidentModel>>

interface FireStoreService {
    suspend fun getAll(email: String): Incidents
    suspend fun get(email: String, incidentId: String): IncidentModel?
    suspend fun insert(email: String, incident: IncidentModel): String
    suspend fun update(email: String, incident: IncidentModel)
    suspend fun delete(email: String, incidentId: String)
}