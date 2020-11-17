package com.teste_pratico.cadastrousuario.controller.api;

import com.teste_pratico.cadastrousuario.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {

    @POST("/usuario/cadastrar")
    Call<Usuario> salvarUsuario(@Body Usuario usuario);
}
