package com.example.mobilefaztudo.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.mobilefaztudo.R
import com.example.mobilefaztudo.data.CadastroContratanteBody
import com.example.mobilefaztudo.databinding.ActivityCadastroBinding
import com.example.mobilefaztudo.repository.AuthRepository
import com.example.mobilefaztudo.utils.APIService
import com.example.mobilefaztudo.view_model.CadastroActivityViewModel
import com.example.mobilefaztudo.view_model.CadastroActivityViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CadastroActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener, TextWatcher {
    private lateinit var mBinding: ActivityCadastroBinding
    private lateinit var mViewModel: CadastroActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCadastroBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.nomeEt.onFocusChangeListener = this
        mBinding.sobrenomeEt.onFocusChangeListener = this
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.telefoneEt.onFocusChangeListener = this
        mBinding.cepEt.onFocusChangeListener = this
        mBinding.cpfEt.onFocusChangeListener = this
        mBinding.logradouroEt.onFocusChangeListener = this
        mBinding.estadoEt.onFocusChangeListener = this
        mBinding.cidadeEt.onFocusChangeListener = this
        mBinding.dataDeNascimentoEt.onFocusChangeListener = this
        mBinding.senhaEt.onFocusChangeListener = this
        mBinding.confirmeSenhaEt.onFocusChangeListener = this
        mBinding.trabalharCheckbox.onFocusChangeListener = this
        mBinding.contratarCheckbox.onFocusChangeListener = this
        mBinding.confirmeSenhaEt.setOnKeyListener(this)
        mBinding.confirmeSenhaEt.addTextChangedListener(this)
        mBinding.cadastroBtn.setOnClickListener(this)
        mViewModel = ViewModelProvider(
            this,
            CadastroActivityViewModelFactory(AuthRepository(APIService.getService()), application)
        ).get(CadastroActivityViewModel::class.java)
        setupObservers()
        preencherDadosAutomaticamente()
    }

    private fun preencherDadosAutomaticamente() {
        val body = CadastroContratanteBody(
            name = "Emerson",
            lastName = "Sobrenome",
            phone = "11984059682",
            cpf = "27526427333",
            dt_nascimento = LocalDate.now(),
            cep = "09450000",
            logradouro = "Bangu",
            state = "Rio de Janeiro",
            city = "Cidade",
            email = "usuario12@email.com",
            senha = "senha123",
            proUser = false
        )
        mViewModel.cadastroContratante(body)
    }

    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this) {
            mBinding.progressBar.isVisible = it
        }
        mViewModel.getErrorMessage().observe(this) {
            val formErrorKeys = arrayOf(
                "name",
                "lastName",
                "cpf",
                "dt_nascimento",
                "cep",
                "lougradouro",
                "state",
                "city",
                "phone",
                "email",
                "senha"
            )
            val message = StringBuilder()
            it.map { entry ->
                if (formErrorKeys.contains(entry.key)) {
                    when (entry.key) {
                        "name" -> {
                            mBinding.nomeTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "lastName" -> {
                            mBinding.sobrenomeTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "email" -> {
                            mBinding.emailTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "phone" -> {
                            mBinding.telefoneTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "cep" -> {
                            mBinding.cepTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "state" -> {
                            mBinding.estadoTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "city" -> {
                            mBinding.cidadeTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "logradouro" -> {
                            mBinding.logradouroTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "cpf" -> {
                            mBinding.cpfTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "dt_nascimento" -> {
                            mBinding.dataDeNascimentoTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "senha" -> {
                            mBinding.senhaTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "confirmeSenha" -> {
                            mBinding.confirmeSenhaTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }
                if (message.isNotEmpty()) {
                    AlertDialog.Builder(this).setIcon(R.drawable.info_24).setTitle("Informação")
                        .setMessage(message)
                        .setPositiveButton("OK") { dialog, _ -> dialog!!.dismiss() }.show()
                }
            }
        }

        mViewModel.getUserC().observe(this) {
            if (it != null) {
                startActivity(Intent(this, HomeCActivity::class.java))
            }
        }
    }

    private fun validateEmail(): Boolean {
        var errorMessage: String? = null
        val value = mBinding.emailEt.text.toString().trim()
        if (value.isEmpty()) {
            mBinding.emailTil.apply {
                isErrorEnabled = true
                error = "E-mail não inserido"
            }
            Log.d("EMAIL", "NAO INSERIDO")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            mBinding.emailTil.apply {
                isErrorEnabled = true
                error = "E-mail inválido"
            }
            Log.d("EMAIL", "INVALIDO")
            return false
        } else {
            mBinding.emailTil.isErrorEnabled = false  // Limpa qualquer erro anterior
            return true  // Email válido, retorna verdadeiro indicando sucesso na validação
        }
    }

    private fun validateSenha(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.senhaEt.text.toString().trim()
        if (value.isEmpty()) {
            errorMessage = "Senha não inserida"
        } else if (value.length < 6) {
            errorMessage = "Senha deve ter 6 caracteres"
        }
        if (errorMessage != null && shouldUpdateView) {
            mBinding.senhaTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null

    }

    private fun validateNome(): Boolean {
        val value: String =
            mBinding.nomeEt.text.toString().trim()  // Remove espaços em branco no início e no final
        var errorMessage: String? = null
        if (value.isEmpty()) {
            mBinding.nomeTil.apply {
                isErrorEnabled = true
                error = "Nome não inserido"
            }
            return false  // Nome vazio, retorna falso indicando erro
        } else if (value.any { it.isDigit() }) {
            mBinding.nomeTil.apply {
                isErrorEnabled = true
                error = "Nome não deve conter números"
            }
            return false  // Nome contém números, retorna falso indicando erro
        } else if (value.any { !it.isLetter() }) {
            mBinding.nomeTil.apply {
                isErrorEnabled = true
                error = "Nome não deve conter caracteres especiais"
            }
            return false  // Nome contém caracteres especiais, retorna falso indicando erro
        }
        mBinding.nomeTil.isErrorEnabled = false
        return true
    }

    private fun validateEstado(): Boolean {
        val value: String =
            mBinding.estadoEt.text.toString()
                .trim()  // Remove espaços em branco no início e no final
        var errorMessage: String? = null
        if (value.isEmpty()) {
            mBinding.nomeTil.apply {
                isErrorEnabled = true
                error = "Estado não inserido"
            }
            return false  // Estado vazio, retorna falso indicando erro
        } else if (value.any { it.isDigit() }) {
            mBinding.cidadeTil.apply {
                isErrorEnabled = true
                error = "Cidade não deve conter números"
            }
            return false  // Cidade contém números, retorna falso indicando erro
        }
        mBinding.estadoTil.isErrorEnabled = false
        return true
    }

    private fun validateCidade(): Boolean {
        val value: String =
            mBinding.cidadeEt.text.toString()
                .trim()  // Remove espaços em branco no início e no final
        var errorMessage: String? = null
        if (value.isEmpty()) {
            mBinding.cidadeTil.apply {
                isErrorEnabled = true
                error = "Cidade não inserido"
            }
            return false  // Cidade vazio, retorna falso indicando erro
        } else if (value.any { it.isDigit() }) {
            mBinding.cidadeTil.apply {
                isErrorEnabled = true
                error = "Cidade não deve conter números"
            }
            return false  // Cidade contém números, retorna falso indicando erro
        }
        mBinding.cidadeTil.isErrorEnabled = false
        return true
    }

    private fun validateLogradouro(): Boolean {
        val value: String =
            mBinding.logradouroEt.text.toString()
                .trim()  // Remove espaços em branco no início e no final
        var errorMessage: String? = null
        if (value.isEmpty()) {
            mBinding.logradouroTil.apply {
                isErrorEnabled = true
                error = "Logradouro não inserido"
            }
            return false  // Logradouro vazio, retorna falso indicando erro
        } else if (value.any { it.isDigit() }) {
            mBinding.logradouroTil.apply {
                isErrorEnabled = true
                error = "Logradouro não deve conter números"
            }
            return false  // Logradouro contém números, retorna falso indicando erro
        }
        mBinding.logradouroTil.isErrorEnabled = false
        return true
    }

    private fun validateSobrenome(): Boolean {
        val value: String = mBinding.sobrenomeEt.text.toString()
            .trim()  // Remove espaços em branco no início e no final
        if (value.isEmpty()) {
            mBinding.sobrenomeTil.apply {
                isErrorEnabled = true
                error = "Sobrenome não inserido"
            }
            return false  // Sobrenome vazio, retorna falso indicando erro
        } else if (value.any { it.isDigit() }) {
            mBinding.sobrenomeTil.apply {
                isErrorEnabled = true
                error = "Sobrenome não deve conter números"
            }
            return false  // Sobrenome contém números, retorna falso indicando erro
        } else if (value.any { !it.isLetter() }) {
            mBinding.sobrenomeTil.apply {
                isErrorEnabled = true
                error = "Sobrenome não deve conter caracteres especiais"
            }
            return false  // Sobrenome contém caracteres especiais, retorna falso indicando erro
        }
        // Sobrenome válido, limpa qualquer erro anteriormente definido
        mBinding.sobrenomeTil.isErrorEnabled = false
        return true
    }

    private fun validateTelefone(): Boolean {
        val value: String = mBinding.telefoneEt.text.toString()
            .trim()  // Remove espaços em branco no início e no final
        if (value.isEmpty()) {
            mBinding.telefoneTil.apply {
                isErrorEnabled = true
                error = "Telefone não inserido"
            }
            return false  // Telefone vazio, retorna falso indicando erro
        } else if (!value.matches(Regex("\\d+"))) {
            mBinding.telefoneTil.apply {
                isErrorEnabled = true
                error = "Telefone inválido: deve conter apenas números"
            }
            return false  // Telefone não corresponde ao formato esperado, retorna falso indicando erro
        }
        // Telefone válido, limpa qualquer erro anteriormente definido
        mBinding.telefoneTil.isErrorEnabled = false
        return true
    }

    private fun validateCpf(): Boolean {
        val value: String =
            mBinding.cpfEt.text.toString().trim() // Remove espaços em branco no início e no final
        if (value.isEmpty()) {
            mBinding.cpfTil.apply {
                isErrorEnabled = true
                error = "CPF não inserido"
            }
            return false
        } else if (!value.matches(Regex("\\d{11}"))) {
            mBinding.cpfTil.apply {
                isErrorEnabled = true
                error = "CPF inválido: formato incorreto"
            }
            return false  // CPF não corresponde ao formato esperado, retorna falso indicando erro
        }
        mBinding.cpfTil.isErrorEnabled = false
        return true
    }

    private fun validateCep(): Boolean {
        val value: String = mBinding.cepEt.text.toString().trim()
        if (value.isEmpty()) {
            mBinding.cepTil.apply {
                isErrorEnabled = true
                error = "CEP não inserido"
            }
            return false  // CEP vazio, retorna falso indicando erro
        } else if (!value.matches(Regex("\\d{8}"))) {
            mBinding.cepTil.apply {
                isErrorEnabled = true
                error = "CEP inválido: formato incorreto"
            }
            return false  // CEP não corresponde ao formato esperado, retorna falso indicando erro
        }
        mBinding.cepTil.isErrorEnabled = false
        return true
    }

    private fun validateDataDeNascimento(): Boolean {
        val value: String = mBinding.dataDeNascimentoEt.text.toString().trim()
        if (value.isEmpty()) {
            mBinding.dataDeNascimentoTil.apply {
                isErrorEnabled = true
                error = "Data de nascimento não inserida"
            }
            return false  // Data de nascimento vazia, retorna falso indicando erro
        } else if (!value.matches(Regex("\\d{2}-\\d{2}-\\d{4}"))) {
            mBinding.dataDeNascimentoTil.apply {
                isErrorEnabled = true
                error = "Data de nascimento inválida: formato incorreto (DD-MM-AAAA)"
            }
            return false  // Data de nascimento não corresponde ao formato esperado, retorna falso indicando erro
        }
        mBinding.dataDeNascimentoTil.isErrorEnabled = false
        return true
    }

    private fun validateSenhasIguais(shouldUpdateView: Boolean = true): Boolean {
        val senha: String = mBinding.senhaEt.text.toString()
        val confirmacaoSenha: String = mBinding.confirmeSenhaEt.text.toString()
        var errorMenssage: String? = null

        if (senha != confirmacaoSenha) {
            errorMenssage = "As senhas não coincidem"
        }
        if (errorMenssage != null && shouldUpdateView) {
            mBinding.confirmeSenhaTil.apply {
                isErrorEnabled = true
                error = errorMenssage

            }
        }
        return errorMenssage == null
    }

    private fun validateConfirmeSenha(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.confirmeSenhaEt.text.toString().trim()
        if (value.isEmpty()) {
            errorMessage = "Senha não inserida"
        } else if (value.length < 6) {
            errorMessage = "Senha inserida deve conter 6 caracteres"
        }
        if (errorMessage != null && shouldUpdateView) {
            mBinding.confirmeSenhaTil.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun convertDateFormat(dateString: String): LocalDate? {
        return try {
            val originalFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            LocalDate.parse(dateString, originalFormat)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    override fun onClick(view: View?) {
        if (view != null && view.id == R.id.cadastroBtn)
            onSubmit()
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.nomeEt -> {
                    if (!hasFocus) {
                        validateNome()
                    } else {
                        mBinding.nomeTil.isErrorEnabled = false
                    }
                }

                R.id.sobrenomeEt -> {
                    if (!hasFocus) {
                        validateSobrenome()
                    } else {
                        mBinding.sobrenomeTil.isErrorEnabled = false
                    }
                }

                R.id.emailEt -> {
                    if (!hasFocus) {
                        validateEmail()
                    } else {
                        mBinding.emailTil.isErrorEnabled = false
                    }
                }

                R.id.telefoneEt -> {
                    if (!hasFocus) {
                        validateTelefone()
                    } else {
                        mBinding.telefoneTil.isErrorEnabled = false
                    }
                }

                R.id.cpfEt -> {
                    if (!hasFocus) {
                        validateCpf()
                    } else {
                        mBinding.cpfTil.isErrorEnabled = false
                    }
                }

                R.id.cepEt -> {
                    if (!hasFocus) {
                        validateCep()
                    } else {
                        mBinding.cepTil.isErrorEnabled = false
                    }
                }

                R.id.estadoEt -> {
                    if (!hasFocus) {
                        validateEstado()
                    } else {
                        mBinding.estadoTil.isErrorEnabled = false
                    }
                }

                R.id.logradouroEt -> {
                    if (!hasFocus) {
                        validateLogradouro()
                    } else {
                        mBinding.logradouroTil.isErrorEnabled = false
                    }
                }

                R.id.cidadeEt -> {
                    if (!hasFocus) {
                        validateCidade()
                    } else {
                        mBinding.cidadeTil.isErrorEnabled = false
                    }
                }

                R.id.dataDeNascimentoEt -> {
                    if (!hasFocus) {
                        validateDataDeNascimento()
                    } else {
                        mBinding.dataDeNascimentoTil.isErrorEnabled = false
                    }
                }

                R.id.senhaEt -> {
                    if (!hasFocus) {
                        if (validateEmail()) {

                        }
                        validateSenha()
                    } else {
                        mBinding.senhaTil.isErrorEnabled = false

                    }
                }


                R.id.confirmeSenhaEt -> {
                    if (!hasFocus) {
                        // Validar a própria confirmação de senha (por exemplo, checar se está vazia ou atende a certos critérios)
                        if (!validateConfirmeSenha()) {
                            mBinding.confirmeSenhaTil.error =
                                "Erro específico para confirmação de senha"
                        } else {
                            // Se a confirmação de senha é válida, verifica se ela coincide com a senha
                            if (!validateSenhasIguais()) {
                                mBinding.confirmeSenhaTil.error = "As senhas não coincidem"
                            } else {
                                // Tudo está correto, limpa erros e pode, opcionalmente, mostrar um ícone de sucesso
                                mBinding.confirmeSenhaTil.isErrorEnabled = false

                                mBinding.confirmeSenhaTil.apply {
                                    setStartIconDrawable(R.drawable.check_circle_24)
//                                    setStartIconTintList(ColorStateList.valueOf(Color.Green))
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP) {
            onSubmit()
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (validateSenha(shouldUpdateView = false) && validateConfirmeSenha(shouldUpdateView = false) && validateConfirmeSenha(
                shouldUpdateView = false
            )
        ) {
            mBinding.confirmeSenhaTil.apply {
                if (isErrorEnabled) isErrorEnabled = false
                setStartIconDrawable(R.drawable.check_circle_24)
            }
        } else {
            if (mBinding.confirmeSenhaTil.startIconDrawable != null) mBinding.confirmeSenhaTil.startIconDrawable =
                null
        }
        Log.d("TAG", "onTextChanged: $s")
    }

    override fun afterTextChanged(s: Editable?) {
    }

    private fun onSubmit() {
        var formattedDate = convertDateFormat(mBinding.dataDeNascimentoEt.text.toString())

        Log.d("TAGE", "onSubmit: Chamando cadastroContratante()")
        Log.d("TAGE", "Nome: ${mBinding.nomeEt.text}")
        Log.d("TAGE", "Sobrenome: ${mBinding.sobrenomeEt.text}")
        Log.d("TAGE", "Email: ${mBinding.emailEt.text}")
        Log.d("TAGE", "Telefone: ${mBinding.telefoneEt.text}")
        Log.d("TAGE", "CPF: ${mBinding.cpfEt.text}")
        Log.d("TAGE", "CEP: ${mBinding.cepEt.text}")
        Log.d("TAGE", "Estado: ${mBinding.estadoEt.text}")
        Log.d("TAGE", "Cidade: ${mBinding.cidadeEt.text}")
        Log.d("TAGE", "Logradouro: ${mBinding.logradouroEt.text}")
        Log.d("TAGE", "Data de Nascimento: $formattedDate")

        if (validate()) {
            val dateToSend = formattedDate ?: LocalDate.now()
            mViewModel.cadastroContratante(
                CadastroContratanteBody(
                    mBinding.nomeEt.text!!.toString(),
                    mBinding.sobrenomeEt.text!!.toString(),
                    mBinding.cpfEt.text!!.toString(),
                    dateToSend,
                    mBinding.cepEt.text!!.toString(),
                    mBinding.logradouroEt.text!!.toString(),
                    mBinding.cidadeEt.text!!.toString(),
                    mBinding.estadoEt.text!!.toString(),
                    mBinding.telefoneEt.text!!.toString(),
                    mBinding.emailEt.text!!.toString(),
                    mBinding.senhaEt.text!!.toString()
                )
            )
        }
    }

    private fun validate(): Boolean {
        Log.d("TAGE", "validate() sendo chamado")
        var isValid = true
        if (!validateNome()) isValid = false
        if (!validateSobrenome()) isValid = false
        if (!validateEmail()) isValid = false
        if (!validateTelefone()) isValid = false
        if (!validateEstado()) isValid = false
        if (!validateCidade()) isValid = false
        if (!validateLogradouro()) isValid = false
        if (!validateCpf()) isValid = false
        if (!validateCep()) isValid = false
        if (!validateDataDeNascimento()) isValid = false
        if (!validateSenha()) isValid = false
        if (!validateConfirmeSenha()) isValid = false
        if (isValid && !validateSenhasIguais()) isValid = false
        Log.d("TAGE", "validate() retornando $isValid")
        return isValid
    }
}