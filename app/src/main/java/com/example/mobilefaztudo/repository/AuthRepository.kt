package com.example.mobilefaztudo.repository

import com.example.mobilefaztudo.data.CadastroContratanteBody
import com.example.mobilefaztudo.data.CadastroPrestadorBody
import com.example.mobilefaztudo.data.LoginBody
import com.example.mobilefaztudo.utils.APIConsumer
import com.example.mobilefaztudo.utils.RequestStatus
import com.example.mobilefaztudo.utils.SimplifieldMessage
import kotlinx.coroutines.flow.flow

class AuthRepository(private val consumer: APIConsumer) {

    fun validateLogin(body: LoginBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateLogin(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifieldMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun cadastroPrestador(body: CadastroPrestadorBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.cadastroPrestador(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifieldMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

    fun cadastroContratante(body: CadastroContratanteBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.cadastroContratante(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else {
            emit(
                RequestStatus.Error(
                    SimplifieldMessage.get(
                        response.errorBody()!!.byteStream().reader().readText()
                    )
                )
            )
        }
    }

}
