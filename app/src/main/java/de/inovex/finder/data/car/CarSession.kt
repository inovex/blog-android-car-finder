package de.inovex.finder.data.car

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session
import de.inovex.finder.ui.car.MainGridScreen

class CarSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen {
        return MainGridScreen(carContext)
    }
}