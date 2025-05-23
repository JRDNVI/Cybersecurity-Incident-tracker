package ie.setu.incident_tracker.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import com.google.firebase.storage.FirebaseStorage
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.device.OfflineDeviceRepository
import ie.setu.incident_tracker.data.firebase.auth.AuthRepository
import ie.setu.incident_tracker.data.firebase.database.FireStoreRepository
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import ie.setu.incident_tracker.data.firebase.storage.StorageRepository
import ie.setu.incident_tracker.data.incident.IncidentRepository
import ie.setu.incident_tracker.data.incident.OfflineIncidentRepository
import ie.setu.incident_tracker.data.location.LocationRepository
import ie.setu.incident_tracker.data.location.LocationService
import ie.setu.incident_tracker.data.retrofit.CveRepository
import ie.setu.incident_tracker.data.retrofit.CveService
import ie.setu.incident_tracker.data.retrofit.ServiceEndpoints
import ie.setu.incident_tracker.data.user.OfflineUserRepository
import ie.setu.incident_tracker.data.user.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val incidentRepository : IncidentRepository
    val deviceRepository : DeviceRepository
    val userRepository : UserRepository
    val authRepository: AuthService
    val credentialManager: CredentialManager
    val credentialRequest: GetCredentialRequest
    val fireStoreRepository: FireStoreService
    val storageRepository: StorageRepository
    val cveRepository : CveRepository
    val locationRepository: LocationRepository

}

class AppDataContainer(private val context: Context) : AppContainer {

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ServiceEndpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val cveService: CveService by lazy {
        retrofit.create(CveService::class.java)
    }


    override val incidentRepository: IncidentRepository by lazy {
        OfflineIncidentRepository(IncidentDatabase.getDatabase(context).incidentDao())
    }

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(IncidentDatabase.getDatabase(context).deviceDao())
    }

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(IncidentDatabase.getDatabase(context).userDao())
    }

    override val authRepository: AuthService by lazy {
        AuthRepository(
            firebaseAuth = FirebaseAuth.getInstance(),
            storageService = storageRepository
        )
    }

    override val fireStoreRepository: FireStoreService by lazy {
        FireStoreRepository(
            firestore,
            authRepository
        )
    }

    override val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    private val googleIdOption: GetGoogleIdOption by lazy {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()
    }

    override val credentialRequest: GetCredentialRequest by lazy {
        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    override val storageRepository: StorageRepository by lazy {
        StorageRepository(firebaseStorage)
    }

    override val cveRepository: CveRepository by lazy {
        CveRepository(cveService)
    }

    private val locationClient: FusedLocationProviderClient by lazy {
        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }

    override val locationRepository: LocationRepository by lazy {
        LocationRepository(locationClient)
    }
}