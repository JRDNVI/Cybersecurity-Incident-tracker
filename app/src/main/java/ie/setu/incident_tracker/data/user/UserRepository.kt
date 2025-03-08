package ie.setu.incident_tracker.data.user

import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

interface UserRepository {
        fun getAllItemsStream(): Flow<List<User>>


        fun getItemStream(id: Int): Flow<User?>


        fun getUserByName(username: String):Flow<User?>


        suspend fun insertItem(user: User)


        suspend fun deleteItem(user: User)


        suspend fun updateItem(user: User)
}