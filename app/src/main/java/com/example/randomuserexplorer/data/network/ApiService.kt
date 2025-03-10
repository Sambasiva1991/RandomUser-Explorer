package com.example.randomuserexplorer.data.network

import com.example.randomuserexplorer.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

        //For Pagination
        @GET("api/")
        suspend fun getUsers(
                @Query("results") results: Int,  // Number of users per request
                @Query("seed") seed: String = "randomuser"  // Keeps data consistent
        ): UserResponse
}