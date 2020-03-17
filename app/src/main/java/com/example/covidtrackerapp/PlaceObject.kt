package com.example.covidtrackerapp

import com.google.gson.annotations.SerializedName

data class PlaceObject(@SerializedName("region")val region:String,
                  @SerializedName("city_province")val city_province:String,
                  @SerializedName("confirmed")val confirmed:String) {
}