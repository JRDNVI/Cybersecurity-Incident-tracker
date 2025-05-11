package ie.setu.incident_tracker

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.setu.incident_tracker.ui.navigation.IncidentTrackerNavHost

@Composable
fun IncidentTrackerApp(
    navController: NavHostController = rememberNavController(),
    onToggleDarkMode: () -> Unit = {}
) {
    IncidentTrackerNavHost(
        navController = navController,
        onToggleDarkMode = onToggleDarkMode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentTrackerTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    onLogout: () -> Unit = {},
    onToggleDarkMode: () -> Unit = {},
    onToggleListAll: () -> Unit = {}
) {
    var expand by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { expand = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(24.dp))
            }

            var darkModeEnabled by remember { mutableStateOf(false) }
            var listAllEnabled by remember { mutableStateOf(false) }

            DropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Mode", fontSize = 16.sp)
                            Switch(
                                checked = darkModeEnabled,
                                onCheckedChange = {
                                    darkModeEnabled = it
                                    onToggleDarkMode()
                                },
                                modifier = Modifier.scale(0.80f)
                            )
                        }
                    },
                    onClick = {}
                )

                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("List All", fontSize = 16.sp)
                            Switch(
                                checked = listAllEnabled,
                                onCheckedChange = {
                                    listAllEnabled = it
                                    onToggleListAll()
                                },
                                modifier = Modifier.scale(0.80f)
                            )
                        }
                    },
                    onClick = {}
                )

                Divider()

                DropdownMenuItem(
                    text = { Text("Logout", fontSize = 16.sp) },
                    onClick = {
                        expand = false
                        onLogout()
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun IncidentTrackerBottomBar(
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit,
    additionalIcons: List<Pair<ImageVector, () -> Unit>> = emptyList()
) {
    BottomAppBar(
        modifier = Modifier.height(64.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        IconButton(onClick = navigateToHome) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Navigate Home"
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = navigateToProfile) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Navigate to Profile",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        additionalIcons.forEach { (icon, action) ->
            IconButton(onClick = action) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Additional Icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}