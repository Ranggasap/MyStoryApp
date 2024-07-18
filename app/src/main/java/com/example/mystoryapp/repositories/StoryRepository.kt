package com.example.mystoryapp.repositories

import com.example.mystoryapp.data.api.RemoteDataSourceStory
import com.example.mystoryapp.data.api.responses.StoryResponse
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryRepository private constructor(
    private val remoteDataSourceStory: RemoteDataSourceStory
) {

    suspend fun getStories(page: Int?, size: Int?): Result<StoryResponse> {
        return remoteDataSourceStory.getStories(page, size)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(remoteDataSourceStory: RemoteDataSourceStory): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(remoteDataSourceStory).also { instance = it }
            }
    }
}