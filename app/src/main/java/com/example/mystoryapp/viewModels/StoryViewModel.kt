package com.example.mystoryapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.api.responses.Story
import com.example.mystoryapp.data.api.responses.StoryResponse
import com.example.mystoryapp.repositories.StoryRepository
import com.example.mystoryapp.utils.Result
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _storyResponseData = MutableLiveData<Result<List<Story>>>()
    val storyResponseData: LiveData<Result<List<Story>>> = _storyResponseData

    init {
        getStories()
    }

    private fun getStories() {
        viewModelScope.launch {
            _storyResponseData.postValue(Result.Loading())
            when(val result = storyRepository.getStories(null, null)){
                is Result.Success -> {
                    _storyResponseData.postValue(Result.Success(result.data?.listStory ?: emptyList()))
                    Log.d("StoryViewModel", "Fetched ${result.data?.listStory?.size} stories")
                }

                is Result.Error -> _storyResponseData.postValue(Result.Error(result.message ?: "Unknown Error"))
                is Result.Loading -> {}
            }
        }
    }
}