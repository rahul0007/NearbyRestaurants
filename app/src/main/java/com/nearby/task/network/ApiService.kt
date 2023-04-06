package com.nearby.task.network
import com.nearby.task.model.response.PlacesPOJO
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("place/nearbysearch/json?")
    fun doPlaces(
        @Query(value = "type", encoded = true) type: String?,
        @Query(value = "location", encoded = true) location: String?,
        @Query(value = "name", encoded = true) name: String?,
        @Query(value = "opennow", encoded = true) opennow: Boolean,
        @Query(value = "rankby", encoded = true) rankby: String?,
        @Query(value = "key", encoded = true) key: String?
    ):Call<PlacesPOJO>

}