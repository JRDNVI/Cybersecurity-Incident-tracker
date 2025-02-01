package ie.setu.incident_tracker.data.incident

import kotlinx.coroutines.flow.Flow

class OfflineIncidentRepository(private val incidentDao: IncidentDao) : IncidentRepository {
    override fun getAllItemsStream(): Flow<List<Incident>> = incidentDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Incident?> = incidentDao.getItem(id)

    override suspend fun insertItem(guest: Incident) = incidentDao.insert(guest)

    override suspend fun deleteItem(guest: Incident) = incidentDao.delete(guest)

    override suspend fun updateItem(guest: Incident) = incidentDao.update(guest)
}