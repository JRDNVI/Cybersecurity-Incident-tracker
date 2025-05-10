package ie.setu.incident_tracker.ui.auth.login

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.firebase.auth.Response
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination

object LoginScreenDestination : NavigationDestination {
    override val route = "Login"
    override val titleRes = R.string.login
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun LoginScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val context = LocalContext.current
    val loginFlow by viewModel.loginFlow.collectAsState()
    val state = viewModel.loginUIState.value

    LaunchedEffect(loginFlow) {
        when (val result = loginFlow) {
            is Response.Success -> {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen()
                viewModel.resetLoginFlow()
            }
            is Response.Failure -> {
                Toast.makeText(context, result.e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                viewModel.resetLoginFlow()
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        viewModel.signInWithGoogleCredentials(context)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(LoginUIEvent.EmailChanged(it)) },
                    label = { Text(stringResource(id = R.string.email)) },
                    isError = state.emailError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(LoginUIEvent.PasswordChanged(it)) },
                    label = { Text(stringResource(id = R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.passwordError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { /* TODO: Forgot password */ }) {
                    Text(text = stringResource(id = R.string.forgot_password))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                    },
                    enabled = viewModel.allValidationsPassed.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.login))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navigateToRegisterScreen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.no_account_register))
                }
            }
        }
    }
}

