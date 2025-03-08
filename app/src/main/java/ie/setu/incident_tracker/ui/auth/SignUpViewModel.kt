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

    suspend fun signUp(user: SignUpState) : Boolean {
        try {
            if (!validateSignUp(user.username, user.password)) {
                return false
            }
            userRepository.insertItem(user.toUser())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun validateSignUp(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            _signUpUiState.value = _signUpUiState.value.copy(errorMessage = "Username and password cannot be blank")
            return false
        } else if (password.length < 2) {
            _signUpUiState.value = _signUpUiState.value.copy(errorMessage = "Password must be at least 2 characters long")
            return false
        }
        return true
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
    val password: String = "",
    val errorMessage: String = ""
)