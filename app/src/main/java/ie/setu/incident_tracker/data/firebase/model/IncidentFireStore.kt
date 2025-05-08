package ie.setu.incident_tracker.data.firebase.model

import androidx.room.Entity
import com.google.firebase.firestore.DocumentId

@Entity
data class IncidentFireStore(
    @DocumentId val _id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val dateOfOccurrence: String = "",
    val location: String = "",
    val longitude: Float = 0f,
    val latitude: Float = 0f,
    val status: Boolean = false,
    val email: String = "",
    val devices: List<DeviceFireStore> = emptyList()
)



