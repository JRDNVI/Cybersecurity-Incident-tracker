package ie.setu.incident_tracker.ui.profile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ie.setu.incident_tracker.IncidentTrackerBottomBar
import ie.setu.incident_tracker.IncidentTrackerTopAppBar
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.auth.login.LoginViewModel
import ie.setu.incident_tracker.ui.auth.register.RegisterViewModel
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ie.setu.incident_tracker.ui.components.ShowPhotoPicker


object ProfileScreenDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile_screen
}
@Composable
fun ProfileScreen (
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.factory),
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.factory),
    registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val profileUiState by viewModel.profileUiSate.collectAsState()
    var photoUri: Uri? by remember { mutableStateOf(viewModel.photoUri) }



    Scaffold(
        topBar = {
        IncidentTrackerTopAppBar(
            title = "Profile",
            canNavigateBack = true,
            navigateUp = navigateBack
        )
    },
    bottomBar = {
        IncidentTrackerBottomBar(
            navigateToHome = { navigateHome() },
            navigateToProfile = {  },
        )
    }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ProfileContent(
                signOut = { viewModel.viewModelScope.launch {
                    viewModel.signOut()
                    onSignOut()
                    registerViewModel.resetRegisterFlow()
                    loginViewModel.resetLoginFlow()
                }},
                photoUri = photoUri,
                onPhotoUriChanged = { photoUri = it },
                displayName = profileUiState.name,
                email = profileUiState.email,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProfileContent(
    signOut: () -> Unit,
    photoUri: Uri?,
    onPhotoUriChanged: (Uri) -> Unit,
    displayName: String,
    email: String,
    viewModel: ProfileViewModel,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = Crop,
            modifier = Modifier.clip(CircleShape).width(180.dp).height(180.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = displayName,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = email,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        ShowPhotoPicker(
            onPhotoUriChanged = {
                onPhotoUriChanged(it)
                viewModel.updatePhotoUri(it)
            }
        )


        Button(
            onClick = {
                signOut()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text(text = "Logout")
        }
    }
}
