package com.example.mobilefaztudo.view;

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mobilefaztudo.R
import com.example.mobilefaztudo.databinding.ActivityLoginBinding
import com.example.mobilefaztudo.repository.AuthRepository
import com.example.mobilefaztudo.utils.APIService
import com.example.mobilefaztudo.view_model.CadastroActivityViewModel
import com.example.mobilefaztudo.view_model.CadastroActivityViewModelFactory
import com.example.mobilefaztudo.view_model.LoginActivityViewModel
import com.example.mobilefaztudo.view_model.LoginActivityViewModelFactory

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.senhaEt.onFocusChangeListener = this
        mViewModel= ViewModelProvider(this, LoginActivityViewModelFactory(AuthRepository(APIService.getService()),application)).get(LoginActivityViewModel::class.java)
setupObservers()

    }

    private fun setupObservers(){
        mViewModel.getIsLoading().observe(this){

        }

        mViewModel.getErrorMessage().observe(this){

        }

        mViewModel.getUser().observe(this){

        }
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.emailEt.text.toString().trim()
        if (value.isEmpty()) {
            errorMessage = "E-mail não inserido"
            Log.d("EMAIL", "NAO INSERIDO")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "E-mail inválido"
            Log.d("EMAIL", "INVALIDO")
        }
        if (errorMessage != null) {
            mBinding.emailTil.apply {
                isErrorEnabled = true
                errorMessage = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateSenha(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.senhaEt.text.toString().trim()
        if (value.isEmpty()) {
            errorMessage = "Senha não inserida"
            Log.d("SENHA", "NAO INSERIDA")
        } else if (value.length < 6) {
            errorMessage = "Senha inválida"
            Log.d("SENHA", "INVALIDA")
        }
        if (errorMessage != null) {
            mBinding.senhaTil.apply {
                isErrorEnabled = true
                errorMessage = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateSenhaEmpty() {
        val value: String = mBinding.senhaEt.text.toString()
        if (value.isEmpty()) {
            mBinding.senhaTil.apply {
                isErrorEnabled = true
                error = "Senha não inserida"
            }
        }
    }

    private fun validateEmailEmpty() {
        val value = mBinding.emailEt.text.toString()
        if (value.isEmpty()) {
            mBinding.emailTil.apply {
                isErrorEnabled = true
                error = "E-mail não inserido"
            }
        }
    }

    override fun onClick(view: View?) {

    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.emailEt -> {
                    if (!hasFocus) {
                        validateEmailEmpty()
                        Log.d("ONFOCUS", "MENSAGEM:::VALIDADEOK:::EMAIL")
                    } else {
                        mBinding.emailTil.isErrorEnabled = false
                    }
                }

                R.id.senhaEt -> {
                    if (!hasFocus) {
                        validateSenhaEmpty()
                        Log.d("ONFOCUS", "MENSAGEM:::VALIDADEOK:::SENHA")
                    } else {
                        mBinding.senhaTil.isErrorEnabled = false
                        Log.d("ONFOCUS", "MENSAGEM:::FALSE:::SENHA")

                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, KeyEvent: KeyEvent?): Boolean {
        return false
    }
}
