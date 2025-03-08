package ie.setu.incident_tracker.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _signInUiState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInUiState.asStateFlow()

    suspend fun userAuth(username: String, password: String): Boolean {
        val user = userRepository.getUserByName(username).firstOrNull()
        Log.d("SignInViewModel", "User retrieved: $user")

        val isAuthenticated = user != null && user.password == password

        _signInUiState.value = SignInState(
            username = username,
            isAuth = isAuthenticated,
            errorMessage = if (isAuthenticated) "" else "Invalid Details"
        )

        return isAuthenticated
    }

    fun updateUiState(signInState: SignInState) {
        _signInUiState.value = SignInState(username = signInState.username, password = signInState.password, isAuth = false, errorMessage = "")
    }

}

data class SignInState(
    val username: String = "",
    val password: String = "",
    val isAuth: Boolean = false,
    val errorMessage: String = ""
)