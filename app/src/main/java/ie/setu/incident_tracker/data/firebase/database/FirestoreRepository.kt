package ie.setu.incident_tracker.data.firebase.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.dataObjects
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.services.Incidents
import ie.setu.incident_tracker.data.rules.Constants.INCIDENT_COLLECTION
import ie.setu.incident_tracker.data.rules.Constants.USER_EMAIL
import java.util.Date

class FireStoreRepository(
    private val firestore: FirebaseFirestore
) : FireStoreService {

    override suspend fun getAll(email: String): Incidents {
        return firestore.collection(INCIDENT_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .dataObjects()
    }

    override suspend fun get(email: String, incidentId: String): Incident? {
        return firestore.collection(INCIDENT_COLLECTION)
            .document(incidentId).get().await().toObject()
    }

    override suspend fun insert(email: String, incident: Incident) {
        val incidentWithEmail = incident.copy(email = email)
        firestore.collection(INCIDENT_COLLECTION)
            .add(incidentWithEmail).await()
    }

    override suspend fun update(email: String, incident: Incident) {
        val updatedIncident = incident.copy(dateOfOccurrence = Date().toString()) // update logic as needed
        firestore.collection(INCIDENT_COLLECTION)
            .document(incident.incidentID.toString())
            .set(updatedIncident).await()
    }

    override suspend fun delete(email: String, incidentId: String) {
        firestore.collection(INCIDENT_COLLECTION)
            .document(incidentId).delete().await()
    }
}
