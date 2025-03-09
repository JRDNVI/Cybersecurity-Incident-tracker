package ie.setu.incident_tracker.ui.device

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.device.Device
import ie.setu.incident_tracker.data.device.DeviceRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditDeviceViewModel(
    savedStateHandle: SavedStateHandle,
    private val deviceRepository: DeviceRepository

) : ViewModel() {

    var deviceUiState by mutableStateOf(DeviceUiState())
        private set

    private val deviceID: Int = checkNotNull(savedStateHandle[EditDeviceDestination.deivceIdArg])

    init {
        viewModelScope.launch {
            deviceUiState = deviceRepository.getItemStream(deviceID)
                .filterNotNull()
                .first()
                .toDeviceUiState(true)
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
                deviceDetails = deviceDetails,
                isEntryValid = validateInput(deviceDetails)
            )
    }

    suspend fun updateItem() {
        if (validateInput(deviceUiState.deviceDetails)) {
            deviceRepository.updateItem(deviceUiState.deviceDetails.toItem())
        }
    }
}

fun Device.toDeviceUiState(isEntryValid: Boolean = false): DeviceUiState = DeviceUiState(
    deviceDetails = this.toDeviceDetails(),
    isEntryValid = isEntryValid
)


fun Device.toDeviceDetails(): DeviceDetails = DeviceDetails(
    deviceID = deviceID,
    name = name,
    ipAddress = ipAddress,
    macAddress = macAddress,
    operatingSystem = operatingSystem,
    cveNumber = cveNumber,
    incidentID = incidentID
)