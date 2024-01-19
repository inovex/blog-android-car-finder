package de.inovex.finder.ui.car

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.PlaceListMapTemplate
import androidx.car.app.model.Template
import de.inovex.finder.R
import de.inovex.finder.data.POIRepository
import de.inovex.finder.data.getLocation
import de.inovex.finder.ui.ListUiState
import de.inovex.finder.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaceListMapScreen(carContext: CarContext) : Screen(carContext), KoinComponent {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val repository: POIRepository by inject()

    private var uiState = ListUiState(
        loading = true
    )

    init {
        loadResults()
    }

    private fun loadResults() {
        getLocation(carContext) {
            if (it != null) {
                coroutineScope.launch {
                    repository.getNearBy("ice cream", it.latitude, it.longitude).onEach { res ->
                        uiState = when (res) {
                            is Resource.Error -> {
                                ListUiState(error = res.errorMessage)
                            }

                            is Resource.Loading -> {
                                ListUiState(
                                    pois = uiState.pois, loading = true
                                )
                            }

                            is Resource.Success -> {
                                ListUiState(
                                    pois = res.data,
                                )
                            }
                        }
                        invalidate()
                    }.launchIn(coroutineScope)
                }
            }
        }
    }

    override fun onGetTemplate(): Template {
        if (!uiState.error.isNullOrEmpty()) {
            return buildErrorMessageTemplate(uiState.error!!)
        }

        val builder = PlaceListMapTemplate.Builder()
            .setTitle(carContext.getString(R.string.app_name))
            .setHeaderAction(Action.BACK)
            .setLoading(uiState.loading)
            .setCurrentLocationEnabled(true)

        if (!uiState.loading) {
            val itemListBuilder = ItemList.Builder()
            uiState.pois.forEach {
                val item = buildPOIItem(it) { poi ->
                    screenManager.push(
                        DetailPaneScreen(
                            carContext, poi
                        )
                    )
                }
                itemListBuilder.addItem(item)
            }
            builder.setItemList(itemListBuilder.build())
        }

        return builder.build()
    }
}