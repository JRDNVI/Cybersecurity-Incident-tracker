package ie.setu.incident_tracker.ui.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.data.firebase.auth.Response
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination

object RegisterScreenDestination : NavigationDestination {
    override val route = "Register"
    override val titleRes = R.string.register
}

@Composable
fun RegisterScreen(
    onRegister: () -> Unit = {},
    navigateToHomeScreen: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val context = LocalContext.current
    val registerFlow = viewModel.signupFlow.collectAsState()
    val state = viewModel.registrationUIState.value
    val isLoading = viewModel.signUpInProgress.value

    LaunchedEffect(registerFlow.value) {
        when (val result = registerFlow.value) {
            is Response.Success -> {
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen()
            }
            is Response.Failure -> {
                Toast.makeText(context, result.e.message ?: "Registration failed.", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { viewModel.onEvent(RegisterUIEvent.FirstNameChanged(it)) },
                    label = { Text("First Name") },
                    isError = state.firstNameError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onEvent(RegisterUIEvent.EmailChanged(it)) },
                    label = { Text("Email") },
                    isError = state.emailError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onEvent(RegisterUIEvent.PasswordChanged(it)) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.passwordError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.privacyPolicyAccepted,
                        onCheckedChange = {
                            viewModel.onEvent(RegisterUIEvent.PrivacyPolicyCheckBoxClicked(it))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Accept terms and conditions")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(RegisterUIEvent.RegisterButtonClicked)
                    },
                    enabled = viewModel.allValidationsPassed.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Register")
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
