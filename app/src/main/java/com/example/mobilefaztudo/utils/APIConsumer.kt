package com.example.mobilefaztudo.utils

import com.example.mobilefaztudo.data.CadastroContratanteBody
import com.example.mobilefaztudo.data.CadastroContratanteResponse
import com.example.mobilefaztudo.data.CadastroPrestadorBody
import com.example.mobilefaztudo.data.CadastroPrestadorResponse
import com.example.mobilefaztudo.data.LoginBody
import com.example.mobilefaztudo.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    fun validateEmailAddress()

    @POST("/auth/login")
    suspend fun validateLogin(@Body body: LoginBody): Response<LoginResponse>

    @POST("/auth/register-service-provider")
    suspend fun cadastroPrestador(@Body body: CadastroPrestadorBody): Response<CadastroPrestadorResponse>

    @POST("/auth/register-contractor")
    suspend fun cadastroContratante(@Body body: CadastroContratanteBody): Response<CadastroContratanteResponse>
}