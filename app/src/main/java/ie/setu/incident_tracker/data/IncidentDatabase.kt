package ie.setu.incident_tracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ie.setu.incident_tracker.data.incident.Incident
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceDao
import ie.setu.incident_tracker.data.incident.IncidentDao

@Database(entities = [Incident::class, Device::class], version = 1)
abstract class IncidentDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao
    abstract fun deviceDao(): DeviceDao

    companion object {
        @Volatile
        private var Instance: IncidentDatabase? = null

        fun getDatabase(context: Context): IncidentDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, IncidentDatabase::class.java, "incident_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}