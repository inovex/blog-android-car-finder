package de.inovex.finder.ui.car

import android.content.Intent
import android.net.Uri
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarColor
import androidx.car.app.model.CarIcon
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import de.inovex.finder.domain.model.POI
import org.koin.core.component.KoinComponent

class DetailPaneScreen(carContext: CarContext, val poi: POI) : Screen(carContext), KoinComponent {

    override fun onGetTemplate(): Template {
        val row = Row.Builder()
            .setTitle(poi.name)
            .addText(poi.address)
            .addText(poi.location)
            .setImage(CarIcon.APP_ICON)
            .build()

        return PaneTemplate.Builder(
            Pane.Builder()
                .addRow(row)
                .addAction(buildNavigationAction(poi.lat, poi.long))
                .addAction(buildBackAction())
                .build()
        )
            .setTitle("Details")
            .setHeaderAction(Action.BACK)
            .build()
    }

    private fun buildNavigationAction(lat: Double, long: Double): Action {
        return Action.Builder()
            .setTitle("Navigation")
            .setOnClickListener {
                val intent = Intent().apply {
                    action = CarContext.ACTION_NAVIGATE
                    data = Uri.parse("geo:$long,$lat")
                }
                carContext.startCarApp(intent)
                screenManager.pop()
            }
            .setBackgroundColor(CarColor.BLUE)
            .build()
    }

    private fun buildBackAction(): Action {
        return Action.Builder()
            .setTitle("Back")
            .setOnClickListener {
                screenManager.pop()
            }.build()
    }
}