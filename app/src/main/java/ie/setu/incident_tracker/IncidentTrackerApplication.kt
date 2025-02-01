package ie.setu.incident_tracker

import android.app.Application
import ie.setu.incident_tracker.data.AppContainer
import ie.setu.incident_tracker.data.AppDataContainer


class IncidentTrackerApplication : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}