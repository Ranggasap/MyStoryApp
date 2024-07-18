package com.example.mystoryapp.data.api

import com.example.mystoryapp.data.api.responses.LoginResponse
import com.example.mystoryapp.data.api.responses.RegisterResponse
import com.example.mystoryapp.utils.Result
import java.lang.Exception

class RemoteDataSourceUser(apiConfigUser: ApiConfigUser) {

    private val apiConfigUser = apiConfigUser.getApiService()

   suspend fun loginUser(email: String, password: String): Result<LoginResponse>{
       return try {
           val response = apiConfigUser.login(email, password)
           print(response)
           if (!response.error) {
               val body = response
               if (body != null){
                   Result.Success(body)
               } else {
                   Result.Error("Login gagal, coba lagi")
               }
           } else {
               Result.Error("Kesalahan dalam melakukan login")
           }
       } catch (e: Exception){
           print("error:$e")
           Result.Error("Kesalahan dalam melakukan login")
       }
   }

    suspend fun registerUser(name: String, email: String, password: String): Result<RegisterResponse>{
        return try {
            val response = apiConfigUser.register(name, email, password)
            if (!response.error) {
                val body = response
                if (body != null){
                    Result.Success(body)
                } else {
                    Result.Error("Registrasi gagal coba lagi")
                }
            } else {
                Result.Error("Kesalahan dalam melakukan registrasi")
            }
        } catch (e: Exception) {
            Result.Error("kesalahan dalam melakukan registrasi")
        }
    }
}