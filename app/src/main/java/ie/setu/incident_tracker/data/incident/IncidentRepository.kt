package ie.setu.incident_tracker.data.incident

import kotlinx.coroutines.flow.Flow

interface IncidentRepository {
    fun getAllItemsStream(): Flow<List<Incident>>


    fun getItemStream(id: Int): Flow<Incident?>


    suspend fun insertItem(guest: Incident)


    suspend fun deleteItem(guest: Incident)


    suspend fun updateItem(guest: Incident)
}