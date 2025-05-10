package ie.setu.incident_tracker.ui.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.firebase.model.DeviceFireStore
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import kotlinx.coroutines.launch

class EditDeviceViewModel(
    savedStateHandle: SavedStateHandle,
    private val fireStoreRepository: FireStoreService,
    private val authRepository: AuthService
) : ViewModel() {

    var deviceUiState by mutableStateOf(DeviceUiState())
        private set

    private val deviceID: String = checkNotNull(savedStateHandle[EditDeviceDestination.deivceIdArg])
    private val incidentID: String = checkNotNull(savedStateHandle[EditDeviceDestination.incidentIdArg])
    private val userEmail = authRepository.email!!

    init {
        viewModelScope.launch {
            val incident = fireStoreRepository.get(userEmail, incidentID)
            val device = incident?.devices?.find { it.deviceID == deviceID }

            device?.let {
                deviceUiState = DeviceUiState(
                    deviceDetails = it.toDeviceDetails(),
                    isEntryValid = validateInput(it.toDeviceDetails())
                )
            }
        }
    }

    private fun validateInput(uiState: DeviceDetails = deviceUiState.deviceDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && ipAddress.isNotBlank() && macAddress.isNotBlank() &&
                    operatingSystem.isNotBlank() && cveNumber.isNotBlank()
        }
    }

    fun updateUiState(deviceDetails: DeviceDetails) {
        deviceUiState = DeviceUiState(
            deviceDetails = deviceDetails,
            isEntryValid = validateInput(deviceDetails)
        )
    }

    suspend fun updateItem() {
        if (validateInput()) {
            val updatedDevice = deviceUiState.deviceDetails.toDeviceFireStore().copy(deviceID = deviceID)
            fireStoreRepository.updateDeviceInIncident(incidentID, updatedDevice)
        }
    }
}


fun Device.toDeviceUiState(isEntryValid: Boolean = false): DeviceUiState = DeviceUiState(
    deviceDetails = this.toDeviceDetails(),
    isEntryValid = isEntryValid
)

fun DeviceFireStore.toDeviceDetails(): DeviceDetails = DeviceDetails(
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
)


fun Device.toDeviceDetails(): DeviceDetails = DeviceDetails(
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
)

fun DeviceDetails.toDeviceFireStore(): DeviceFireStore = DeviceFireStore(
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
)
