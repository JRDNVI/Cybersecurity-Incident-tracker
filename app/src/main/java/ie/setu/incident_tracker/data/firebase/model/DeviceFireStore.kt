package ie.setu.incident_tracker.data.firebase.model

import androidx.room.Entity
import com.google.firebase.firestore.DocumentId

@Entity
data class DeviceFireStore(
 @DocumentId val _id: String = "",
    val name: String = "",
    val ipAddress: String = "",
    val macAddress: String = "",
    val operatingSystem: String = "",
    val cveNumber: String = "",
    val incidentID: String = "",
)

