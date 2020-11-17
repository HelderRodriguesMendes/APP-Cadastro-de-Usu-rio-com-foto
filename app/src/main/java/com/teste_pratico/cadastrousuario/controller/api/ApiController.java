package com.teste_pratico.cadastrousuario.controller.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
    public Retrofit baseURL() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder().baseUrl("http:192.168.1.2:8080/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }
}
