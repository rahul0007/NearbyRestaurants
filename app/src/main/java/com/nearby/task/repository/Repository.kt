package com.nearby.task.repository

import com.nearby.task.model.request.LoginRequest
import com.nearby.task.network.ApiService
import javax.inject.Inject


class Repository @Inject constructor(private val apiService: ApiService) {
    //    flow task on background
    suspend fun doPlaces(placeType:String,latLng:String,businessName:String,distance:String,ApiKey:String)=apiService.doPlaces(placeType,latLng,businessName,true,distance,ApiKey)
}