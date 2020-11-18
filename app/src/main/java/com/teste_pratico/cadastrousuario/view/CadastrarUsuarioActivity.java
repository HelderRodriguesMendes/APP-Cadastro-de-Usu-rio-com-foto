package com.teste_pratico.cadastrousuario.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teste_pratico.cadastrousuario.R;
import com.teste_pratico.cadastrousuario.controller.Config;
import com.teste_pratico.cadastrousuario.controller.api.ApiController;
import com.teste_pratico.cadastrousuario.controller.api.UsuarioService;
import com.teste_pratico.cadastrousuario.model.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CadastrarUsuarioActivity extends AppCompatActivity {

    private int ano, mes, dia;
    private String DATA, TITULO_ALERT, MSG_ALERT, STATUS_ALERT, OPCAO1_ALERT, OPCAO2_ALERT;
    private static String fotoEmString, STATUS_FORM;
    private static byte[] fotoEmBytes;
    private static Long ID_USUARIO;
    private boolean addFoto = false;

    Config config = new Config();
    ApiController URL_BASE_API = new ApiController();
    Usuario usuario;

    UsuarioService usuarioService = URL_BASE_API.baseURL().create(UsuarioService.class);

    private TextView textViewDataNascimento, textViewTitle;
    private EditText editTextNome;
    private ImageView IMAGEVIEW_FOTO;
    private Button button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        //EXIBE O BOTÃO VOLTAR <--
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CadastrarUsuarioActivity.this.setTitle(R.string.title_home);

        textViewDataNascimento = findViewById(R.id.textViewDataNascimento);
        IMAGEVIEW_FOTO = findViewById(R.id.imageViewFOTO);
        editTextNome = findViewById(R.id.editTextTextPersonName);
        textViewTitle = findViewById(R.id.textViewTitleCad);
        button = findViewById(R.id.button);

        configForm();

        //EXIBE O CALENDARIO PARA ADICIONAR A DATA DE NASCIMENTO
        final Calendar calendar = Calendar.getInstance();
        textViewDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ano = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                dia = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastrarUsuarioActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        DATA = i2 + "/" + (i1 + 1) + "/" + i;
                        textViewDataNascimento.setText(config.configDataApp(DATA));
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        //ADICIONAR FOTO
        IMAGEVIEW_FOTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MSG_ALERT = "Deseja utilizar a Câmera ou a Galeria?";
                TITULO_ALERT = "FOTO DO USUÁRIO";
                STATUS_ALERT = "foto";
                OPCAO1_ALERT = "Câmera";
                OPCAO2_ALERT = "Galeria";

                msgAlert(TITULO_ALERT, MSG_ALERT, STATUS_ALERT, OPCAO1_ALERT, OPCAO2_ALERT);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (validarCampus()) {
                    if (STATUS_FORM.equals("cadastrar")) {
                        salvar(preencherObjeto());
                    } else {
                        alterar(preencherObjeto());
                    }
                }
            }
        });
    }

    //CONFIGURA O FORM PARA O REAPROVEITAMENTO DE TELA
    public void configForm() {
        if (STATUS_FORM.equals("cadastrar")) {
            textViewTitle.setText(R.string.title_cad);

        } else if (STATUS_FORM.equals("alterar")) {
            usuario = new Usuario();

            //RECEBENDO OS DADOS VINDOS DA ACTIVITY DE LISTAGEM
            usuario = getIntent().getExtras().getParcelable("usuario");
            textViewTitle.setText(R.string.title_alte);
            preencherCampus(usuario);
            button.setText("ALTERAR");
        }
    }

    //RECEBE A FOTO TIRADA PELA CAMERA E EXIBE ESSA FOTO NA LETA DE CADASTRO
    protected void onActivityResult(int requestCode, int resultCode, Intent dados) {
        super.onActivityResult(requestCode, resultCode, dados);
        if (requestCode == 1) {
            try {
                //RECEBENDO A FOTO
                Bitmap fotoTiradaNaCamera = (Bitmap) dados.getExtras().get("data");

                //EXIBINDO A FOTO NA TELA
                IMAGEVIEW_FOTO.setImageBitmap(fotoTiradaNaCamera);

                ByteArrayOutputStream streamFotoEmBytes = new ByteArrayOutputStream();

                //TRANFORMANDO A FOTO EM PNG E ARMAZENDANDO ELA NO streamFotoEmBytes
                fotoTiradaNaCamera.compress(Bitmap.CompressFormat.PNG, 70, streamFotoEmBytes);
                fotoEmBytes = streamFotoEmBytes.toByteArray();
                fotoEmString = Base64.encodeToString(fotoEmBytes, Base64.DEFAULT);

            } catch (Exception e) {

            }
        }
    }

    //VALIDA O PREENCHIMENTO DOS CAMPUS
    public boolean validarCampus() {
        boolean validacaoConcluida = false;

        if (editTextNome.getText().toString().equals("")) {
            editTextNome.setError("Preenchimento Obrigatório");
            editTextNome.requestFocus();
        } else if (textViewDataNascimento.getText().toString().equals("")) {
            textViewDataNascimento.setError("Preenchimento Obrigatório");
            Toast.makeText(CadastrarUsuarioActivity.this, "Informe a Data de Nascimento", Toast.LENGTH_SHORT).show();
        } else if (!addFoto && STATUS_FORM.equals("cadastrar")) {
            Toast.makeText(CadastrarUsuarioActivity.this, "Adicione a foto do Usuário", Toast.LENGTH_SHORT).show();
            IMAGEVIEW_FOTO.requestFocus();
        } else {
            validacaoConcluida = true;
        }
        return validacaoConcluida;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Usuario preencherObjeto() {
        Usuario usuario = new Usuario();

        usuario.setNome(editTextNome.getText().toString());
        String data = textViewDataNascimento.getText().toString();
        usuario.setDataNascimento(config.configDataApi(data));
        usuario.setFoto(fotoEmString);
        usuario.setAtivo(true);
        if (STATUS_FORM.equals("alterar")) {
            usuario.setId(ID_USUARIO);
        }

        return usuario;
    }

    private void preencherCampus(Usuario usuario) {
        editTextNome.setText(usuario.getNome());
        textViewDataNascimento.setText(usuario.getDataNascimento());
        IMAGEVIEW_FOTO.setImageBitmap(config.converteString_Foto(usuario.getFoto()));
        fotoEmString = usuario.getFoto();
        ID_USUARIO = usuario.getId();
    }

    public void salvar(Usuario usuario) {

        Call<Usuario> call = usuarioService.cadastrar(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    MSG_ALERT = "Usuário cadastrado com sucesso";
                    TITULO_ALERT = "CADASTRO DE USUÁRIO";
                    STATUS_ALERT = "cadastrar";
                    msgSucesso(TITULO_ALERT, MSG_ALERT, STATUS_ALERT);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Erro ao salvar: " + t.getMessage());

            }
        });
    }

    public void alterar(Usuario usuario) {
        Call<Usuario> call = usuarioService.alterar(usuario.getId(), usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    MSG_ALERT = "Usuário alterado com sucesso";
                    TITULO_ALERT = "ALTERAR USUÁRIO";
                    STATUS_ALERT = "alterar";
                    msgSucesso(TITULO_ALERT, MSG_ALERT, STATUS_ALERT);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    public void msgSucesso(String titulo, String msg, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarUsuarioActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(CadastrarUsuarioActivity.this).inflate(
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
                if (STATUS_FORM.equals("cadastrar")) {
                    Intent intent = new Intent(CadastrarUsuarioActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CadastrarUsuarioActivity.this, List_usuarios.class);
                    List_usuarios.statusForm("alterar");
                    startActivity(intent);
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
    public void msgAlert(final String titulo, String msg, final String status, String opcao1, String opcao2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarUsuarioActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(CadastrarUsuarioActivity.this).inflate(
                R.layout.alert_inform, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.txtMessage)).setText(msg);

        ((Button) view.findViewById(R.id.btnYes)).setText(opcao1);
        ((Button) view.findViewById(R.id.btnNo)).setText(opcao2);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (status.equals("foto")) {
                    addFoto = true;

                    //ABRINDO A CAMERA DO CELULAR
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (status.equals("foto")) {
                    addFoto = true;
                }
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    //RECEBE O STATUS DO FORM (CADASTRAR/ALTERAR)
    public static void statusForm(String status) {
        STATUS_FORM = status;
    }
}