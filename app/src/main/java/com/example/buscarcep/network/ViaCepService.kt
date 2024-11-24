package com.example.buscarcep.network

import com.example.buscarcep.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json/")
    fun buscarEndereco(@Path("cep") cep: String): Call<Endereco>
}