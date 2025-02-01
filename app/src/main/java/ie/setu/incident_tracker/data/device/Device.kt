package ie.setu.incident_tracker.data.device

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ie.setu.incident_tracker.data.incident.Incident

@Entity(
    tableName = "devices",
    foreignKeys = [ForeignKey(
        entity = Incident::class,
        parentColumns = ["incidentID"],
        childColumns = ["incidentID"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class Device (
    @PrimaryKey(autoGenerate = true)
    val deviceID: Int = 0,
    val name: String,
    val ipAddress: String,
    val macAddress: String,
    val operatingSystem: String,
    val cveNumber: String,
    val incidentID: Int
)