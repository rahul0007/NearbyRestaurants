package com.nearby.task.model.response

import com.google.gson.annotations.SerializedName

class PlacesPOJO {
        @SerializedName("results")
        var customA: List<CustomA> = ArrayList()

        @SerializedName("status")
        var status: String? = null
    }

     class CustomA {
        @SerializedName("geometry")
        var geometry: Geometry? = null

        @SerializedName("vicinity")
        var vicinity: String? = null

        @SerializedName("name")
        var name: String? = null
    }

     class Geometry {
        @SerializedName("location")
        var locationA: LocationA? = null
    }

     class LocationA {
        @SerializedName("lat")
        var lat: String? = null

        @SerializedName("lng")
        var lng: String? = null
}