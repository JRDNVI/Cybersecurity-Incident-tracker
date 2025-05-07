package ie.setu.incident_tracker

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import ie.setu.incident_tracker.data.AppContainer
import ie.setu.incident_tracker.data.AppDataContainer


class IncidentTrackerApplication : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        container = AppDataContainer(this)
    }
}