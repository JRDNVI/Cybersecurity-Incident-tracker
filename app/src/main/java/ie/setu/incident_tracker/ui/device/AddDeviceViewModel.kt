package ie.setu.incident_tracker.ui.device

import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceRepository
import ie.setu.incident_tracker.data.user.UserRepository

class AddDeviceViewModel(
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val incidentID: Int = checkNotNull(savedStateHandle[AddDeviceDestination.IncidentIDArg])

    var deviceUiState by mutableStateOf(DeviceUiState())
        private set

    private fun validateInput(uiState: DeviceDetails = deviceUiState.deviceDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && ipAddress.isNotBlank() && macAddress.isNotBlank() && operatingSystem.isNotBlank() && cveNumber.isNotBlank()
        }
    }

    fun updateUiState(deviceDetails: DeviceDetails) {
        deviceUiState =
            DeviceUiState(
                deviceDetails = deviceDetails.copy(incidentID = incidentID),
                isEntryValid = validateInput(deviceDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            deviceRepository.insertItem(deviceUiState.deviceDetails.toItem())
        }
    }
}

data class DeviceUiState(
    val deviceDetails: DeviceDetails = DeviceDetails(),
    val isEntryValid: Boolean = false
)

data class DeviceDetails(
    val deviceID: Int = 0,
    val name: String = "",
    val ipAddress: String = "",
    val macAddress: String = "",
    val operatingSystem: String = "",
    val cveNumber: String = "",
    val incidentID: Int = 0
)

fun DeviceDetails.toItem(): Device = Device(
    deviceID = deviceID,
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
    incidentID = incidentID
)