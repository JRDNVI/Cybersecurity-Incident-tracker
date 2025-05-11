package ie.setu.incident_tracker.ui.components

import CveResponse
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CveDetailCard(cveResponse: CveResponse, modifier: Modifier = Modifier) {

    val cve = cveResponse.vulnerabilities.firstOrNull()?.cve!!
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "CVE ID: ${cve.id}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description: ${cve.descriptions.firstOrNull()?.value ?: "No description available"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val cvssScore = cve.metrics.cvssMetricV31.firstOrNull()?.cvssData?.baseScore ?: "N/A"
            Text(text = "CVSS Score: $cvssScore", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Severity: ${cve.metrics.cvssMetricV31.firstOrNull()?.cvssData?.baseSeverity ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
