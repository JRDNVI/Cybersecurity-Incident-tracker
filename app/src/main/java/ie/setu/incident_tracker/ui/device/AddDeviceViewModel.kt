package ie.setu.incident_tracker.ui.device

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import kotlinx.coroutines.launch

class AddDeviceViewModel(
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository,
    private val authRepository: AuthService,
    private val fireStoreRepository: FireStoreService
) : ViewModel() {

    private val incidentID: String = checkNotNull(savedStateHandle[AddDeviceDestination.IncidentIDArg])
    private val userEmail = authRepository.email!!


    var deviceUiState by mutableStateOf(DeviceUiState())
        private set

    init {
        viewModelScope.launch {
            try {
                Log.d("Email:", incidentID)
                val firestoreIncident = fireStoreRepository.get(userEmail, incidentID)
                Log.d("AddDevice:", firestoreIncident.toString())
                Log.d("AddDeviceID", incidentID)
            } catch (e : Exception) {
                Log.e("ViewIncidentDetails", "Error fetching Firestore incident", e)
            }
        }
    }

    private fun validateInput(uiState: DeviceDetails = deviceUiState.deviceDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && ipAddress.isNotBlank() && macAddress.isNotBlank() && operatingSystem.isNotBlank() && cveNumber.isNotBlank()
        }
    }

    fun updateUiState(deviceDetails: DeviceDetails) {
        deviceUiState =
            DeviceUiState(
                deviceDetails = deviceDetails.copy(),
                isEntryValid = validateInput(deviceDetails)
            )
    }

    suspend fun saveItem() {
        if (validateInput()) {
            Log.d("ID:", incidentID)
            fireStoreRepository.addDeviceToIncident(
                incidentID,
                deviceUiState.deviceDetails.toItem()
            )
        }
    }

}

data class DeviceUiState(
    val deviceDetails: DeviceDetails = DeviceDetails(),
    val isEntryValid: Boolean = false
)

data class DeviceDetails(
    val name: String = "",
    val ipAddress: String = "",
    val macAddress: String = "",
    val operatingSystem: String = "",
    val cveNumber: String = "",
)

fun DeviceDetails.toItem(): DeviceFireStore = DeviceFireStore(
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
)
