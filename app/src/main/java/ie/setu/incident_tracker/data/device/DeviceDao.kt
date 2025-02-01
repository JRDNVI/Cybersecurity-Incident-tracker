package ie.setu.incident_tracker.data.device

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(device: Device)

    @Update
    suspend fun update(device: Device)

    @Delete
    suspend fun delete(device: Device)

    @Query("SELECT * FROM devices WHERE deviceID = :id")
    fun getItem(id: Int): Flow<Device>

    @Query("SELECT * FROM devices ORDER BY name ASC")
    fun getAllItems(): Flow<MutableList<Device>>
}