package ie.setu.incident_tracker.ui.auth

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SignInDestination : NavigationDestination {
    override val route = "signIn"
    override val titleRes = R.string.home_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val sigInUiState by viewModel.signInState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SignInBody(
                signInUiState = sigInUiState,
                onSignInValueChange = viewModel::updateUiState,
                viewModel = viewModel,
                scope = scope,
                navigateToHomeScreen = navigateToHomeScreen,
                navigateToSignUpScreen = navigateToSignUpScreen,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SignInBody(
    signInUiState: SignInState,
    onSignInValueChange: (SignInState) -> Unit,
    viewModel: SignInViewModel,
    scope: CoroutineScope,
    navigateToHomeScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            OutlinedTextField(
                value = signInUiState.username,
                onValueChange = { onSignInValueChange(signInUiState.copy(username = it)) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = signInUiState.password,
                onValueChange = { onSignInValueChange(signInUiState.copy(password = it))},
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    scope.launch {
                        if (viewModel.userAuth(signInUiState.username, password = signInUiState.password) ) {
                            navigateToHomeScreen()
                        }
                        Log.d("SignInScreen", "User authenticated: ${signInUiState.isAuth}")

                    }
                },

                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Text("Sign In")
            }
        }
            item {
                Button(
                    onClick = navigateToSignUpScreen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Sign Up")
                }
            }

        }
}
