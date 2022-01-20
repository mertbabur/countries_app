package com.example.myapplication.service

import com.example.myapplication.model.Country
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

interface CountryAPI {

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    fun getCountries():Single<List<Country>>


}