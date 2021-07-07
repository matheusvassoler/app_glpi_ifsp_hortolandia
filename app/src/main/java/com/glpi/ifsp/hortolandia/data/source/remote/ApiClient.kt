package com.glpi.ifsp.hortolandia.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://192.168.0.12:8888/glpi/apirest.php/"

class ApiClient {

    operator fun invoke(): Api {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()

        return retrofit.create(Api::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val logging = configureLoggingInterceptor()
        return createOkHttpClient(logging)
    }

    private fun configureLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    private fun createOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}
