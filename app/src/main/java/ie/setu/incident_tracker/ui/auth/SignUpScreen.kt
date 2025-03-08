package ie.setu.incident_tracker.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ie.setu.incident_tracker.R
import ie.setu.incident_tracker.ui.AppViewModelProvider
import ie.setu.incident_tracker.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SignUpDestination : NavigationDestination {
    override val route = "signUp"
    override val titleRes = R.string.SignUp_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val signUpState by viewModel.signUpState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold {
        SignUpBody(
            signUpState = signUpState,
            onSignUpValueChange = viewModel::updateSignUpState,
            viewModel = viewModel,
            scope = scope,
            modifier = modifier
        )
    }
}

@Composable
fun SignUpBody(
    signUpState: SignUpState,
    onSignUpValueChange: (SignUpState) -> Unit,
    viewModel: SignUpViewModel,
    scope: CoroutineScope,
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
                value = signUpState.username,
                onValueChange = { onSignUpValueChange(signUpState.copy(username = it)) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = signUpState.password,
                onValueChange = { onSignUpValueChange(signUpState.copy(password = it))},
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.signUp(signUpState)
                    }
                },

                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Text("Add Incident")
            }
        }
    }
}