package ie.setu.incident_tracker.data.retrofit

import CveResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CveService {
    @GET("cves/2.0")
    suspend fun get(@Query("cveId") cve: String): CveResponse
}
