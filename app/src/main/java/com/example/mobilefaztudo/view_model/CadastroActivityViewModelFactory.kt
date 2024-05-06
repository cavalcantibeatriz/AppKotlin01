package com.example.mobilefaztudo.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilefaztudo.repository.AuthRepository
import java.security.InvalidParameterException

class CadastroActivityViewModelFactory(
    private val authRepository: AuthRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("CADASTRO_UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CadastroActivityViewModel::class.java)) {
            return CadastroActivityViewModel(authRepository, application) as T
        }

        throw InvalidParameterException("Sem construtor de CadastroActivityViewModel")
    }
}