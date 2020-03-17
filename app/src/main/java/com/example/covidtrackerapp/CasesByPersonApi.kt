package com.example.covidtrackerapp

import retrofit2.http.GET

interface CasesByPersonApi {

    @GET("/cases")
    fun getCases ():retrofit2.Call<ArrayList<CaseObject>>

}