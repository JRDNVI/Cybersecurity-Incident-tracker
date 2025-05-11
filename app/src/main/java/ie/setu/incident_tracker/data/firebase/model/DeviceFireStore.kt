package ie.setu.incident_tracker.data.firebase.model

import java.util.UUID

data class DeviceFireStore(
   val deviceID: String = UUID.randomUUID().toString(),
   val name: String = "",
   val ipAddress: String = "",
   val macAddress: String = "",
   val operatingSystem: String = "",
   val cveNumber: String = "",
   var imageUri: String = ""
)

