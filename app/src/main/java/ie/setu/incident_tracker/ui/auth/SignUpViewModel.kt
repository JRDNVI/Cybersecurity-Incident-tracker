package ie.setu.incident_tracker.ui.auth

import androidx.lifecycle.ViewModel
import ie.setu.incident_tracker.data.user.User
import ie.setu.incident_tracker.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signUpUiState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpUiState.asStateFlow()

    suspend fun signUp(user: SignUpState) {
        userRepository.insertItem(user.toUser())
    }

    fun updateSignUpState(signUpState: SignUpState) {
        _signUpUiState.value = signUpState.copy(username = signUpState.username, password = signUpState.password)
    }
}

fun SignUpState.toUser(): User = User(
    username = username,
    password = password,
    admin = false
)

data class SignUpState(
    val username: String = "",
    val password: String = ""
)