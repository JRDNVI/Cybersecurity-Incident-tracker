package ie.setu.incident_tracker.data.incident

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "incidents")
data class Incident (
    @PrimaryKey(autoGenerate = true)
    val incidentID: Int = 0,
    val title: String,
    val description: String,
    val type: String,
    val dateOfOccurrence: Date,
    val location: String,
    val longitude: Float,
    val latitude: Float,
    val status: Boolean
)