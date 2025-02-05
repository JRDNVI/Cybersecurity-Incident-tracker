package ie.setu.incident_tracker.data.user

import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllItemsStream(): Flow<List<User>> = userDao.getAllItems()

    override fun getItemStream(id: Int): Flow<User?> = userDao.getItem(id)

    override fun getUserByName(username: String): Flow<User?> = userDao.getUserByName(username)

    override suspend fun insertItem(guest: User) = userDao.insert(guest)

    override suspend fun deleteItem(guest: User) = userDao.delete(guest)

    override suspend fun updateItem(guest: User) = userDao.update(guest)
}