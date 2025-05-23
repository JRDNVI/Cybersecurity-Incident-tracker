package ie.setu.incident_tracker.data.incident

import android.net.Uri
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
    val dateOfOccurrence: String,
    val location: String,
    val longitude: Float,
    val latitude: Float,
    val status: Boolean,
    val email: String = "",
    val imageUri: Uri = Uri.EMPTY
)