package ie.setu.incident_tracker.data.incident

import androidx.lifecycle.Lifecycle
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(incident: Incident)

    @Update
    suspend fun update(incident: Incident)

    @Delete
    suspend fun delete(incident: Incident)

    @Query("SELECT * FROM incidents WHERE incidentID = :id")
    fun getItem(id: Int): Flow<Incident>

    @Query("SELECT * FROM incidents ORDER BY title ASC")
    fun getAllItems(): Flow<MutableList<Incident>>
}