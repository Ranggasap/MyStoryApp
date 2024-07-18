package com.example.mystoryapp.data.api

import com.example.mystoryapp.data.api.responses.StoryResponse
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.utils.Result
import kotlinx.coroutines.flow.first

class RemoteDataSourceStory(
    private val userPreference: UserPreference
) {

    suspend fun getStories(page: Int?, size: Int?): Result<StoryResponse>{
        return try {
            val token = userPreference.getUserToken().first()
            if(token != null){
                val apiService = ApiConfigStory.getApiService(token)
                val response = apiService.getStories(page, size)
                if(response.isSuccessful){
                    val body = response.body()
                    if (body != null ){
                        Result.Success(body)
                    } else {
                        Result.Error("Data get stories gagal diambil, coba lagi")
                    }
                } else {
                    Result.Error("Kesalahan dalam menarik data stories")
                }
            } else {
                Result.Error("Token tidak ditemukan, tolong login terlebih dahulu")
            }
        } catch (e: Exception) {
            Result.Error("Kesalahan dalam melakukan get stories")
        }
    }

}