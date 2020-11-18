package com.teste_pratico.cadastrousuario.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.teste_pratico.cadastrousuario.R;
import com.teste_pratico.cadastrousuario.controller.AdapterUsuario;
import com.teste_pratico.cadastrousuario.controller.Config;
import com.teste_pratico.cadastrousuario.controller.RecyclerItemClickListener;
import com.teste_pratico.cadastrousuario.controller.api.ApiController;
import com.teste_pratico.cadastrousuario.controller.api.UsuarioService;
import com.teste_pratico.cadastrousuario.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_usuarios extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText txtNome;
    private TextView tituloList;
    private static String STATUS_FORM;

    List<Usuario> USUARIOS = new ArrayList<>();
    Config config = new Config();

    ApiController URL_BASE_API = new ApiController();
    UsuarioService usuarioService = URL_BASE_API.baseURL().create(UsuarioService.class);
    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_usuarios);

        recyclerView = findViewById(R.id.recyclerView);
        txtNome = findViewById(R.id.editTextNome);
        tituloList = findViewById(R.id.txtTituloList);

        if (STATUS_FORM.equals("consultar ativos")) {
            getUsuariosAtivos("");
            tituloList.setText("USUÁRIOS CADASTRADOS");
        }

        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override //Evento ao digitar no EditText
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nome = txtNome.getText().toString();
                getUsuariosAtivos(nome);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getUsuariosAtivos(String nome) {
        Call<List<Usuario>> call = null;
        if (nome.equals("")) {
            call = usuarioService.getUsuariosAtivos();
        } else {
            call = usuarioService.getUsuariosAtivos_NOME(nome);
        }

        call.enqueue(new Callback<List<Usuario>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    USUARIOS = response.body();
                    for (Usuario u : USUARIOS) {
                        LocalDate dt = LocalDate.parse(u.getDataNascimento());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String dataFormatada = dt.format(formatter);
                        u.setDataNascimento(dataFormatada);
                    }
                    listarUsuarios(USUARIOS);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }

    public void listarUsuarios(final List<Usuario> usuarios) {
        AdapterUsuario adapterUsuario = new AdapterUsuario(usuarios);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()); //Gerenciador de Layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterUsuario);

        //EVENTO DE CLICK
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!STATUS_FORM.equals("consultar ativos")) {
                    usuario = USUARIOS.get(position);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    //RECEBE O STATUS DO FORM(LISTAR ATIVOS - LISTAR DESATIVOS)
    public static void statusForm(String status) {
        STATUS_FORM = status;
    }
}