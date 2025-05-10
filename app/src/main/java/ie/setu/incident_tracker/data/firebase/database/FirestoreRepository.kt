package ie.setu.incident_tracker.data.firebase.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import ie.setu.incident_tracker.data.firebase.model.IncidentFireStore
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
import ie.setu.incident_tracker.data.firebase.services.Incidents
import ie.setu.incident_tracker.data.rules.Constants.INCIDENT_COLLECTION
import ie.setu.incident_tracker.data.rules.Constants.USER_EMAIL
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.dataObjects
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
import java.util.Date

class FireStoreRepository(
    private val firestore: FirebaseFirestore
) : FireStoreService {

    override suspend fun getAll(email: String): Incidents {
        return firestore.collection(INCIDENT_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .dataObjects<IncidentFireStore>()
    }

    override suspend fun get(email: String, incidentId: String): IncidentModel? {
        return firestore.collection(INCIDENT_COLLECTION)
            .document(incidentId)
            .get()
            .await()
            .toObject(IncidentFireStore::class.java)
    }

    override suspend fun insert(email: String, incident: IncidentModel): String {
        val docRef = firestore.collection(INCIDENT_COLLECTION).document()
        val data = incident.copy(email = email)
        docRef.set(data).await()
        return docRef.id
    }



    override suspend fun update(email: String, incident: IncidentModel) {
        val updatedIncident = incident.copy(dateOfOccurrence = Date().toString())
        firestore.collection(INCIDENT_COLLECTION)
            .document(incident._id)
            .set(updatedIncident)
            .await()
    }

    override suspend fun delete(email: String, incidentId: String) {
        firestore.collection(INCIDENT_COLLECTION)
            .document(incidentId)
            .delete()
            .await()
    }

    override suspend fun addDeviceToIncident(incidentId: String, device: DeviceFireStore) {
        val incidentRef = firestore.collection(INCIDENT_COLLECTION).document(incidentId)
        val snapshot = incidentRef.get().await()
        val currentIncident = snapshot.toObject(IncidentFireStore::class.java)

        currentIncident?.let {
            val updatedDevices = it.devices + device
            incidentRef.update("devices", updatedDevices).await()

        }
    }

    override suspend fun deleteDeviceFromIncident(incidentId: String, deviceId: String) {
        val incidentRef = firestore.collection(INCIDENT_COLLECTION).document(incidentId)
        val snapshot = incidentRef.get().await()
        val currentIncident = snapshot.toObject(IncidentFireStore::class.java)

        currentIncident?.let {
            val updatedDevices = it.devices.filterNot { device -> device.deviceID == deviceId }
            incidentRef.update("devices", updatedDevices).await()
        }
    }

    override suspend fun updateDeviceInIncident(incidentId: String, updatedDevice: DeviceFireStore) {
        val incidentRef = firestore.collection(INCIDENT_COLLECTION).document(incidentId)
        val snapshot = incidentRef.get().await()
        val currentIncident = snapshot.toObject(IncidentFireStore::class.java)

        currentIncident?.let {
            val newDeviceList = it.devices.map { device ->
                if (device.deviceID == updatedDevice.deviceID) updatedDevice else device
            }
            incidentRef.update("devices", newDeviceList).await()
        }
    }
}
