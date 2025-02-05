package ie.setu.incident_tracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _signInUiState = MutableStateFlow(SignInState())
    val signInState: StateFlow<SignInState> = _signInUiState.asStateFlow()

    fun userAuth(username: String, password: String) {
        viewModelScope.launch {
            userRepository.getUserByName(username)
                .map { user ->
                    if (user != null && user.password == password) {
                        SignInState(username = username, isAuth = true)
                    } else {
                        SignInState(username = username, errorMessage = "Invalid Details")
                    }
                }
                .collect { newState ->
                    _signInUiState.value = newState

                }
        }
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