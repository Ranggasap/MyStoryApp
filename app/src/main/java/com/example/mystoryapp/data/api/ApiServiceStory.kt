package com.example.mystoryapp.data.api

import com.example.mystoryapp.data.api.responses.StoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceStory {
    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ) : Response<StoryResponse>


}