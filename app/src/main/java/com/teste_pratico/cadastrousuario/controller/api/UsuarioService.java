package com.teste_pratico.cadastrousuario.controller.api;

import com.teste_pratico.cadastrousuario.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioService {

    //CADASTRA UM USUARIO
    @POST("/usuario/cadastrar")
    Call<Usuario> cadastrar(@Body Usuario usuario);

    //BUSCA TODOS OS USUARIOS ATIVOS
    @GET("/usuario/usuariosAtivos")
    Call<List<Usuario>> getUsuariosAtivos();

    //BUSCA USUARIOS ATIVOS POR NOME
    @GET("/usuario/usuariosAtivos_NOME?")
    Call<List<Usuario>> getUsuariosAtivos_NOME(@Query("nome") String nome);

    @PUT("/usuario/alterar/{id}")
    Call<Usuario> alterar(@Path("id") Long id, @Body Usuario usuario);

    @PUT("/usuario/desativar/{id}")
    Call<Boolean> desativarUsario(@Path("id") Long id);

    //BUSCA TODOS OS USUARIOS DESATIVADOS
    @GET("/usuario/usuariosDesativados")
    Call<List<Usuario>> getUsuariosDesativados();

    //BUSCA USUARIOS DESATIVADOS POR NOME
    @GET("/usuario/usuariosDesativados_Nome?")
    Call<List<Usuario>> getUsuariosDesativados_NOME(@Query("nome") String nome);

    @PUT("/usuario/ativar/{id}")
    Call<Boolean> ativarUsario(@Path("id") Long id);
}
