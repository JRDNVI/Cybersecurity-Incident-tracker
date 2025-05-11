package ie.setu.incident_tracker.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ie.setu.incident_tracker.data.firebase.services.AuthService
import ie.setu.incident_tracker.data.firebase.services.FireStoreService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val authService: AuthService,
    private val fireStoreService: FireStoreService
) : ViewModel() {

    val currentUser = authService.currentUser
    private val _profileUiState = MutableStateFlow(ProfileUiSate())
    val profileUiSate: StateFlow<ProfileUiSate> = _profileUiState.asStateFlow()

    val photoUri get() = authService.customPhotoUri
    val email get() = authService.email.toString()

    init {
        currentUser?.let { user ->
            _profileUiState.value = ProfileUiSate(
                name = user.displayName.orEmpty(),
                email = user.email.orEmpty(),
                profilePicture = user.photoUrl!!
            )
        }
    }

    suspend fun signOut() {
        authService.signOut()
    }

    fun updatePhotoUri(uri: Uri) {
        viewModelScope.launch {
            authService.updatePhoto(uri)
            fireStoreService.updatePhotoUris(email, photoUri!!)
        }
    }

}

data class ProfileUiSate(
    val name: String = "",
    val email: String = "",
    val profilePicture: Uri = Uri.EMPTY
)
