package ie.setu.incident_tracker.data.retrofit

import CveResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CveRepository(private val api: CveService) {
    suspend fun getCveDetails(id: String): CveResponse {
        return withContext(Dispatchers.IO) {
            api.get(id)
        }
    }
}

