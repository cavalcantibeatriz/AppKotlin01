package com.example.mobilefaztudo.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilefaztudo.data.LoginBody
import com.example.mobilefaztudo.data.LoginResponse
import com.example.mobilefaztudo.data.User
import com.example.mobilefaztudo.repository.AuthRepository
import com.example.mobilefaztudo.utils.RequestStatus
import kotlinx.coroutines.launch

class LoginActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private var user: MutableLiveData<User> = MutableLiveData()
    private var login: MutableLiveData<LoginResponse> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUser(): LiveData<User> = user
    fun getLogin(): LiveData<LoginResponse> = login

    fun validateLogin(body: LoginBody) {
        viewModelScope.launch {
            authRepository.validateLogin(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }

                    is RequestStatus.Success -> {
                        isLoading.value = false

                    }

                    is RequestStatus.Error -> {
                        isLoading.value = false
                    }
                }
            }
        }
    }
}