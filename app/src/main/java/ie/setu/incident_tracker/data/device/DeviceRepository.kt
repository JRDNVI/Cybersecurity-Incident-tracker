package ie.setu.incident_tracker.data.device

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getAllItemsStream(): Flow<List<Device>>


    fun getItemStream(id: Int): Flow<Device?>


    suspend fun insertItem(guest: Device)


    suspend fun deleteItem(guest: Device)


    suspend fun updateItem(guest: Device)
}