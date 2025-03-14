package com.example.randomuserexplorer.data.repository

import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val apiService: ApiService) {

    fun fetchUsers(count: Int): Flow<List<User>> = flow {
        try {
            val response = apiService.getUsers(count)

            if (response.results.isNotEmpty()) {
                emit(response.results)
            } else {
                throw Exception("Empty response from API")
            }
        } catch (e: SocketTimeoutException) {
            emit(emptyList())
            throw Exception("Network timeout. Please try again.")
        } catch (e: Exception) {
            emit(emptyList())
            throw Exception("Network error")
        }
    }.flowOn(Dispatchers.IO)

}