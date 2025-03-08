package ie.setu.incident_tracker.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    val username: String,
    val password: String,
    val admin: Boolean
)