package de.inovex.finder

import com.google.gson.GsonBuilder
import de.inovex.finder.data.KomootApi
import de.inovex.finder.data.POIRepository
import de.inovex.finder.ui.mobile.ListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<KomootApi> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        Retrofit.Builder()
            .baseUrl("https://photon.komoot.io/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(KomootApi::class.java)
    }

    single<POIRepository> {
        POIRepository(get())
    }

    viewModel {
        ListViewModel(get())
    }
}