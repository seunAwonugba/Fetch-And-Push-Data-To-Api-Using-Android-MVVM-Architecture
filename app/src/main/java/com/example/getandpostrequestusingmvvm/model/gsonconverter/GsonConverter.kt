package com.example.getandpostrequestusingmvvm.model.gsonconverter

import com.example.getandpostrequestusingmvvm.model.api.ApiGetInterface
import com.example.getandpostrequestusingmvvm.model.constant.Constant.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Using an object class because i want to make this class singleton
 */
object GsonConverter {
    /**
     * Lazily initialise the retrofit builder and pass OKHTTP in it
     */
    private val retrofit by lazy {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level= HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * create an instance of the interface and lazily initialise it
     *
     * call create function on the instance of retrofit builder that was lazily initialised above
     *
     * and then pass the Api interface
     */

    val passApiInterface: ApiGetInterface by lazy {
        retrofit.create(ApiGetInterface::class.java)
    }
}