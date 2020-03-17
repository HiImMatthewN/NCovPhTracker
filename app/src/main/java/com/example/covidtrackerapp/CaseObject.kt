package com.example.covidtrackerapp

import com.google.gson.annotations.SerializedName

data class CaseObject(@SerializedName("case_no")val case_no:String,
                 @SerializedName("date")val date:String,
                 @SerializedName("age")val age:String,
                 @SerializedName("gender")val gender:String,
                 @SerializedName("nationality")val nationality:String,
                 @SerializedName("hospital_admitted_to")val hospital_admitted_to:String,
                 @SerializedName("had_recent_travel_history_abroad")val had_recent_travel_history_abroad:String,
                 @SerializedName("status")val status:String,
                 @SerializedName("notes")val notes:String ) {
}