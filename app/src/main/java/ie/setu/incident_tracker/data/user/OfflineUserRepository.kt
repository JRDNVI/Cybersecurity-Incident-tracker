package ie.setu.incident_tracker.data.user

import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllItemsStream(): Flow<List<User>> = userDao.getAllItems()

    override fun getItemStream(id: Int): Flow<User?> = userDao.getItem(id)

    override fun getUserByName(username: String): Flow<User?> = userDao.getUserByName(username)

    override suspend fun insertItem(user: User) = userDao.insert(user)

    override suspend fun deleteItem(user: User) = userDao.delete(user)

    override suspend fun updateItem(user: User) = userDao.update(user)
}