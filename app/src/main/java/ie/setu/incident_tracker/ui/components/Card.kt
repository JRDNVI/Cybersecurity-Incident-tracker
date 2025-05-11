package ie.setu.incident_tracker.ui.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ie.setu.incident_tracker.data.firebase.services.IncidentModel
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon as M3Icon
import androidx.compose.material3.Button as M3Button
import androidx.compose.material3.Text as M3Text

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableIncidentCard(
    incident: IncidentModel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val cardHeight = 100.dp
    var showDeleteDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
    ) {
        // https://developer.android.com/reference/kotlin/androidx/compose/material/DismissState
        val dismissState = rememberDismissState(confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    onEdit()
                    false
                }

                DismissValue.DismissedToStart -> {
                    showDeleteDialog = true
                    false
                }

                else -> false
            }
        })

        //https://developer.android.com/reference/kotlin/androidx/compose/material/DismissDirection
        //https://www.geeksforgeeks.org/android-jetpack-compose-swipe-to-dismiss-with-material-3/
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(
                DismissDirection.StartToEnd,
                DismissDirection.EndToStart
            ),
            background = {
                val direction = dismissState.dismissDirection
                val color = when (direction) {
                    DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                    DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                    null -> MaterialTheme.colorScheme.surface
                }

                val icon = when (direction) {
                    DismissDirection.StartToEnd -> Icons.Default.Edit
                    DismissDirection.EndToStart -> Icons.Default.Delete
                    else -> null
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(color)
                        .padding(horizontal = 16.dp),
                    contentAlignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                        else -> Alignment.Center
                    }
                ) {
                    icon?.let {
                        M3Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            dismissContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    IncidentCard(incident, onClick)
                }
            }
        )

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { M3Text("Delete Incident") },
                text = { M3Text("Are you sure you want to delete this incident?") },
                confirmButton = {
                    M3Button(onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }) { M3Text("Delete") }
                },
                dismissButton = {
                    M3Button(onClick = { showDeleteDialog = false }) {
                        M3Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun IncidentCard(incident: IncidentModel, onClick: () -> Unit) {
    val photoUri = Uri.parse(incident.imageUri)

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(photoUri).crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                )
                M3Text(
                    text = incident.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f).padding(6.dp)
                )
                M3Text(
                    text = incident.type,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            M3Text(
                text = if (!incident.status) "Status: Open" else "Status: Closed",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

