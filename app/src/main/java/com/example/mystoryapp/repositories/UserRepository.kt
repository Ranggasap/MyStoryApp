package com.example.mystoryapp.repositories

import com.example.mystoryapp.data.api.RemoteDataSourceUser
import com.example.mystoryapp.data.api.responses.LoginResponse
import com.example.mystoryapp.data.api.responses.LoginResult
import com.example.mystoryapp.data.api.responses.RegisterResponse
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.utils.Result
import kotlinx.coroutines.flow.Flow


class UserRepository private constructor(
    private val remoteDataSourceUser: RemoteDataSourceUser,
    private val userPreference: UserPreference
){
    suspend fun registerUser(name: String, email: String, password: String): Result<RegisterResponse>{
        return remoteDataSourceUser.registerUser(name, email, password)
    }

    suspend fun loginUser(email: String, password: String): Result<LoginResponse>{
        val result = remoteDataSourceUser.loginUser(email, password)
        if (result is Result.Success) {
            if (result.data?.loginResult != null) {
                userPreference.saveUserPreference(result.data.loginResult)
            }
        }
        return result
    }

    fun userSession(): Flow<LoginResult?> {
        return userPreference.getUserPreference()
    }

    suspend fun logoutUser() {
        return userPreference.logoutUser()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(remoteDataSourceUser: RemoteDataSourceUser, userPreference: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(remoteDataSourceUser, userPreference).also { instance = it }
            }
    }
}