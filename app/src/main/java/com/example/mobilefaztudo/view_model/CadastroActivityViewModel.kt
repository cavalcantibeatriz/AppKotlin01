package com.example.mobilefaztudo.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilefaztudo.data.CadastroContratanteBody
import com.example.mobilefaztudo.data.CadastroContratanteResponse
import com.example.mobilefaztudo.data.CadastroPrestadorBody
import com.example.mobilefaztudo.data.CadastroPrestadorResponse
import com.example.mobilefaztudo.data.User
import com.example.mobilefaztudo.repository.AuthRepository
import com.example.mobilefaztudo.utils.RequestStatus
import kotlinx.coroutines.launch

class CadastroActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private val userC: MutableLiveData<CadastroContratanteResponse> = MutableLiveData()
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getUserC(): LiveData<CadastroContratanteResponse> = userC

    fun cadastroContratante(body:CadastroContratanteBody ){
        viewModelScope.launch {
            authRepository.cadastroContratante(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        userC.value = it.data
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
}