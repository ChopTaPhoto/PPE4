package fr.efficom.formation.ppe1719

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createBornesService(): BornesService{
    val urlApi = "http://coralielecocq.fr/"
    val retrofit = Retrofit.Builder().baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val bornesService = retrofit.create(BornesService::class.java)
    return bornesService
}