package com.example.randomuserexplorer.data.network

import com.example.randomuserexplorer.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
        @GET("api/")  // No need to change base URL
        suspend fun getUsers(@Query("results") count: Int): UserResponse
}