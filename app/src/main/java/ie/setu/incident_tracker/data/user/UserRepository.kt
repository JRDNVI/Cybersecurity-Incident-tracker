package ie.setu.incident_tracker.data.user

import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

interface UserRepository {
        fun getAllItemsStream(): Flow<List<User>>


        fun getItemStream(id: Int): Flow<User?>


        fun getUserByName(username: String):Flow<User?>


        suspend fun insertItem(guest: User)


        suspend fun deleteItem(guest: User)


        suspend fun updateItem(guest: User)
}