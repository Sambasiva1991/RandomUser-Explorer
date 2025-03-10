package com.example.randomuserexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomuserexplorer.data.model.User
import com.example.randomuserexplorer.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadUsers(count: Int) {
        viewModelScope.launch {
            _isLoading.value = true  // Start Loading
            _errorMessage.value = null  // Reset error state
            try {
                repository.fetchUsers(count)
                    .onEach { users ->
                        _userList.value = users
                    }
                    .catch { e ->
                        _errorMessage.value = e.message
                    }
                    .collect({}) // Just collect() without a lambda since onEach handles updates
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false  // Always set loading false after operation
            }
        }
    }

//    fun loadUsers(count: Int) {
//        viewModelScope.launch {
//            _isLoading.value = true  // Start Loading
//            _errorMessage.value = null  // Reset error state
//            try {
//                repository.fetchUsers(count)
//                    .catch { e ->
//                        _errorMessage.value = e.message
//                        _isLoading.value = false  // Stop loading if error occurs
//                    }
//                    .collect { users ->
//                        _userList.value = users
//                        _isLoading.value = false  // Stop loading after success
//                    }
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//                _isLoading.value = false
//            }
//        }
//
//    }
//    fun loadUsers(count: Int) {
//
//        println("loadUsers In UserViewModel  >>>>>"+count)
//        viewModelScope.launch {
//            repository.fetchUsers(count)
//                .catch { _userList.value = emptyList() } // Handle error gracefully
//                .collect { users ->
//                    _userList.value = users
//                }
//        }
//    }
}