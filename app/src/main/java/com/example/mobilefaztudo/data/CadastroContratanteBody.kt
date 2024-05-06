package com.example.mobilefaztudo.data

import java.time.LocalDate

data class CadastroContratanteBody(
    val name: String,
    val lastName: String,
    val cpf: String,
    val dt_nascimento: LocalDate,
    val cep: String,
    val logradouro: String,
    val state: String,
    val city: String,
    val phone: String,
    val email: String,
    val senha: String,
    val proUser: Boolean = false
)

