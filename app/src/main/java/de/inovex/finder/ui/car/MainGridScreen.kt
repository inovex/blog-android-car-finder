package de.inovex.finder.ui.car

import android.Manifest
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.CarIcon
import androidx.car.app.model.GridItem
import androidx.car.app.model.GridTemplate
import androidx.car.app.model.ItemList
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import de.inovex.finder.R
import org.koin.core.component.KoinComponent

class MainGridScreen(carContext: CarContext) : Screen(carContext), KoinComponent {
    private val permission = Manifest.permission.ACCESS_FINE_LOCATION
    private var hasLocationPermission = false

    init {
        carContext.requestPermissions(listOf(permission)) { granted, _ ->
            hasLocationPermission = granted.contains(permission)
            invalidate()
        }
    }

    override fun onGetTemplate(): Template {
        if (!hasLocationPermission) {
            return buildErrorMessageTemplate("No location permission given")
        }

        val searchIcon = getCarIconFrom(R.drawable.search)
        val iceCreamIcon = getCarIconFrom(R.drawable.icecream)

        val searchItem = GridItem.Builder()
            .setTitle("Search")
            .setOnClickListener { screenManager.push(SearchScreen(carContext)) }
            .setImage(searchIcon)
            .build()
        val iceCream = GridItem.Builder()
            .setTitle("Get Ice Cream")
            .setOnClickListener { screenManager.push(PlaceListMapScreen(carContext)) }
            .setImage(iceCreamIcon)
            .build()

        val itemList = ItemList.Builder().addItem(searchItem).addItem(iceCream)
        return GridTemplate.Builder()
            .setTitle("Hello!")
            .setLoading(false)
            .setSingleList(itemList.build())
            .build()
    }

    private fun getCarIconFrom(@DrawableRes resIdRes: Int): CarIcon {
        return CarIcon.Builder(
            IconCompat.createFromIcon(
                carContext,
                Icon.createWithResource(
                    carContext,
                    resIdRes
                )
            )!!
        ).build()
    }
}