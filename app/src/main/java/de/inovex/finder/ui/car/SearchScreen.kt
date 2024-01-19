package de.inovex.finder.ui.car

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.SearchTemplate
import androidx.car.app.model.SearchTemplate.SearchCallback
import androidx.car.app.model.Template
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

class SearchScreen(carContext: CarContext) : Screen(carContext), KoinComponent {
    private val repository: POIRepository by inject()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var uiState = ListUiState()

    override fun onGetTemplate(): Template {
        val itemListBuilder = ItemList.Builder()
        uiState.pois.forEach {
            itemListBuilder.addItem(buildPOIItem(it) { poi ->
                screenManager.push(
                    DetailPaneScreen(
                        carContext,
                        poi
                    )
                )
            })
        }

        val searchCallback = object : SearchCallback {
            override fun onSearchSubmitted(searchText: String) {
                search(searchText)
            }
        }
        val builder = SearchTemplate.Builder(searchCallback)
            .setShowKeyboardByDefault(true)
            .setLoading(uiState.loading)
            .setHeaderAction(Action.BACK)

        // Its not allowed to set items while isLoading
        if (uiState.pois.any() && !uiState.loading) {
            builder.setItemList(itemListBuilder.build())
        }

        return builder.build()
    }

    private fun search(text: String) {
        getLocation(carContext) {
            if (it != null) {
                coroutineScope.launch {
                    repository.getNearBy(text, it.latitude, it.longitude).onEach { res ->
                        uiState = when (res) {
                            is Resource.Error -> {
                                ListUiState(error = res.errorMessage)
                            }

                            is Resource.Loading -> {
                                ListUiState(
                                    loading = true
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
}