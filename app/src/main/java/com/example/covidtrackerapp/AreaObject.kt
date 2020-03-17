package com.example.covidtrackerapp

import com.google.gson.annotations.SerializedName

data class AreaObject(@SerializedName("areas")val areas:ArrayList<PlaceObject> ) {
}