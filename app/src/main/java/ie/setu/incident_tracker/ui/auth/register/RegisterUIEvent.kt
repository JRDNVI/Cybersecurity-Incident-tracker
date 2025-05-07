package ie.setu.incident_tracker.ui.auth.register

sealed class RegisterUIEvent{

    data class FirstNameChanged(val firstName:String) : RegisterUIEvent()
    data class EmailChanged(val email:String): RegisterUIEvent()
    data class PasswordChanged(val password: String) : RegisterUIEvent()
    data class PrivacyPolicyCheckBoxClicked(val status:Boolean) : RegisterUIEvent()

    object RegisterButtonClicked : RegisterUIEvent()
}
