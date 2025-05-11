package ie.setu.incident_tracker.ui.cve

import CveResponse
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ie.setu.incident_tracker.data.retrofit.CveRepository

class CveDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val cveRepository : CveRepository
) : ViewModel() {

    private val cveId: String = checkNotNull(savedStateHandle["cveId"])

    suspend fun getDetails(): CveResponse {
        return cveRepository.getCveDetails(cveId)
    }
}