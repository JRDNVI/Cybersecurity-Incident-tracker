package ie.setu.incident_tracker.data.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ie.setu.incident_tracker.data.incident.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE userID = :id")
    fun getItem(id: Int): Flow<User>

    @Query("SELECT * FROM users WHERE username = :username")
    fun getUserByName(username: String): Flow<User>

    @Query("SELECT * FROM users ORDER BY userID ASC")
    fun getAllItems(): Flow<MutableList<User>>
}