package ie.setu.incident_tracker.data.device

import kotlinx.coroutines.flow.Flow

class OfflineDeviceRepository(private val deviceDao: DeviceDao) : DeviceRepository {
    override fun getAllItemsStream(): Flow<List<Device>> = deviceDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Device?> = deviceDao.getItem(id)

    override suspend fun insertItem(guest: Device) = deviceDao.insert(guest)

    override suspend fun deleteItem(guest: Device) = deviceDao.delete(guest)

    override suspend fun updateItem(guest: Device) = deviceDao.update(guest)
}