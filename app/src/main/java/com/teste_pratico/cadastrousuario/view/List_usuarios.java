package com.teste_pratico.cadastrousuario.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String TITULO_ALERT, MSG_ALERT, STATUS_ALERT, OPCAO1_ALERT, OPCAO2_ALERT;
    private boolean RESTAURAR = false;

    List<Usuario> USUARIOS = new ArrayList<>();

    ApiController URL_BASE_API = new ApiController();
    UsuarioService usuarioService = URL_BASE_API.baseURL().create(UsuarioService.class);
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_usuarios);

        //EXIBE O BOTÃO VOLTAR <--
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        txtNome = findViewById(R.id.editTextNome);
        tituloList = findViewById(R.id.txtTituloList);

        configForm();
        List_usuarios.this.setTitle(R.string.title_home);

        //EVENTO AO DIGITAR NO EditText
        txtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override //Evento ao digitar no EditText
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String nome = "";
                nome = txtNome.getText().toString();

                if(STATUS_FORM.equals("excluir")){
                    getUsuariosDesativos(nome);
                }else{
                    getUsuariosAtivos(nome);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //CONFIGURANDO FORM PARA O REAPROVEITAMENTO DE TELA
    public void configForm(){
        if (STATUS_FORM.equals("consultar ativos")) {
            getUsuariosAtivos("");
            tituloList.setText("USUÁRIOS CADASTRADOS");
        } else if (STATUS_FORM.equals("alterar")) {
            getUsuariosAtivos("");
            tituloList.setText("ALTERAR USUÁRIO SELECIONADO");
        }else if(STATUS_FORM.equals("excluir")){
            getUsuariosAtivos("");
            tituloList.setText("EXCLUIR USUÁRIO SELECIONADO");

            OPCAO1_ALERT = "Excluir";
            OPCAO2_ALERT = "Restaurar";
            TITULO_ALERT = "ATENÇÃO";
            MSG_ALERT = "Deseja Excluir ou Restaurar os dados de um Usuário";
            STATUS_ALERT = "excluir restaurar";
            msgAlert(TITULO_ALERT, MSG_ALERT, STATUS_ALERT, OPCAO1_ALERT, OPCAO2_ALERT);
        }
    }

    //BUSCA USUARIOS ATIVOS NO BANCO
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

    //BUSCA USUARIOS DESATIVADOS NO BANCO
    public void getUsuariosDesativos(String nome) {
        Call<List<Usuario>> call = null;
        if (nome.equals("")) {
            call = usuarioService.getUsuariosDesativados();
        } else {
            call = usuarioService.getUsuariosDesativados_NOME(nome);
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

    //LISTA USUARIOS
    public void listarUsuarios(final List<Usuario> usuarios) {
        AdapterUsuario adapterUsuario = new AdapterUsuario(usuarios);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()); //Gerenciador de Layout
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterUsuario);

        //EVENTO DE CLICK NA LISTA
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!STATUS_FORM.equals("consultar ativos")) {
                    usuario = new Usuario();
                    usuario = USUARIOS.get(position);

                    OPCAO1_ALERT = "Sim";
                    OPCAO2_ALERT = "Não";

                    if (STATUS_FORM.equals("alterar")) {
                        TITULO_ALERT = "ALTERAR DADOS";
                        MSG_ALERT = "Deseja alterar os dados de " + usuario.getNome() + "?";
                        STATUS_ALERT = "alterar user ativo";

                    }else if (STATUS_FORM.equals("excluir")) {
                        if(!RESTAURAR){
                            TITULO_ALERT = "EXCLUIR DADOS";
                            MSG_ALERT = "Deseja excluir os dados de " + usuario.getNome() + "?";
                            STATUS_ALERT = "excluir user";
                        }else{
                            TITULO_ALERT = "RESTAURAR DADOS";
                            MSG_ALERT = "Deseja restaurar os dados de " + usuario.getNome() + "?";
                            STATUS_ALERT = "restaurar";
                        }
                    }
                    msgAlert(TITULO_ALERT, MSG_ALERT, STATUS_ALERT, OPCAO1_ALERT, OPCAO2_ALERT);
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

    //DESATIVA UM USUARIO
    public void desativarUsuario(Long id){
        Call<Boolean> call = usuarioService.desativarUsario(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    boolean ok = response.body();
                    if(ok){
                        TITULO_ALERT = "EXCLUIR USUÁRIO";
                        MSG_ALERT = "EXCLUSÃO REALIZADA COM SUCESSO";
                        STATUS_ALERT = "excluir";
                        msgSucesso(TITULO_ALERT, MSG_ALERT, STATUS_ALERT);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    //ATIVA UM USUARIO
    public void ativarUsuario(Long id){
        Call<Boolean> call = usuarioService.ativarUsario(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    boolean ok = response.body();
                    if(ok){
                        TITULO_ALERT = "RESTAURAR USUÁRIO";
                        MSG_ALERT = "Usuário restaurado COM SUCESSO";
                        STATUS_ALERT = "restaurar";
                        msgSucesso(TITULO_ALERT, MSG_ALERT, STATUS_ALERT);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void msgSucesso(String titulo, String msg, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_usuarios.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(List_usuarios.this).inflate(
                R.layout.layout_success_dialog, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);
        ((Button) view.findViewById(R.id.btnAction)).setText(getResources().getString(R.string.btnOK_msgSucesso));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("excluir")){
                    getUsuariosAtivos("");
                }else{
                    getUsuariosDesativos("");
                }

                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    //EXIBE UMA MSG DE ESCOLHAS PARA O USUARIO
    public void msgAlert(final String titulo, String msg, final String status, String Opicao1, String opcao2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_usuarios.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(List_usuarios.this).inflate(
                R.layout.alert_inform, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);

        ((Button) view.findViewById(R.id.btnYes)).setText(Opicao1);
        ((Button) view.findViewById(R.id.btnNo)).setText(opcao2);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(status.equals("alterar user ativo")){
                    Intent intent = new Intent(List_usuarios.this, CadastrarUsuarioActivity.class);
                    intent.putExtra("usuario", usuario);
                    CadastrarUsuarioActivity.statusForm("alterar");
                    startActivity(intent);
                }else if(status.equals("excluir user")){
                    desativarUsuario(usuario.getId());
                }else if(status.equals("excluir restaurar")){
                    getUsuariosAtivos("");
                }else if(status.equals("restaurar")){
                    ativarUsuario(usuario.getId());
                }
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(status.equals("excluir restaurar")){
                    getUsuariosDesativos("");
                    tituloList.setText("RESTAURAR DADOS SELECIONADOS");
                    RESTAURAR = true;
                }
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    //RECEBE O STATUS DO FORM(LISTAR ATIVOS - LISTAR DESATIVOS)
    public static void statusForm(String status) {
        STATUS_FORM = status;
    }
}