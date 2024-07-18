package com.example.mystoryapp.viewModels

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.api.responses.LoginResponse
import com.example.mystoryapp.data.api.responses.LoginResult
import com.example.mystoryapp.data.api.responses.RegisterResponse
import com.example.mystoryapp.repositories.UserRepository
import com.example.mystoryapp.utils.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginResponseData = MutableLiveData<Result<LoginResponse>>()
    val loginResponseData: LiveData<Result<LoginResponse>> = _loginResponseData

    private val _registerResponseData = MutableLiveData<Result<RegisterResponse>>()
    val registerResponseData: LiveData<Result<RegisterResponse>> = _registerResponseData

    fun loginUser(email: String, password: String){
        _loginResponseData.value = Result.Loading()
        viewModelScope.launch {
            _loginResponseData.value = userRepository.loginUser(email, password)
        }
    }

    fun registerUser(name: String, email: String, password: String){
        _registerResponseData.value = Result.Loading()
        viewModelScope.launch {
            _registerResponseData.value = userRepository.registerUser(name, email, password)
        }
    }

    fun userSession(): LiveData<LoginResult?>{
        return userRepository.userSession().asLiveData()
    }

    fun logoutUser(){
        viewModelScope.launch {
            return@launch userRepository.logoutUser()
        }
    }
}