package ie.setu.incident_tracker.data.location

import android.location.Location
import com.google.api.Context
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun getLocationFlow(): Flow<Location?>
}