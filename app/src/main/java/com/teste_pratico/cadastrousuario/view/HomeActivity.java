package com.teste_pratico.cadastrousuario.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.teste_pratico.cadastrousuario.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView CAD_USER, PESQUI_USER, EDIT_USER, EXCLU_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeActivity.this.setTitle(R.string.title_home);

        CAD_USER = findViewById(R.id.cad_usuario);
        PESQUI_USER = findViewById(R.id.pesqui_usuario);
        EDIT_USER = findViewById(R.id.edit_usuario);
        EXCLU_USER = findViewById(R.id.exclu_usuario);

        CAD_USER.setOnClickListener(this);
        PESQUI_USER.setOnClickListener(this);
        EDIT_USER.setOnClickListener(this);
        EXCLU_USER.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()) {
            case R.id.cad_usuario:
                i = new Intent(this, CadastrarUsuarioActivity.class);
                CadastrarUsuarioActivity.statusForm("cadastrar");
                startActivity(i);
                break;
            case R.id.pesqui_usuario:
                i = new Intent(this, List_usuarios.class);
                List_usuarios.statusForm("consultar ativos");
                startActivity(i);
                break;
            case R.id.edit_usuario:
                i = new Intent(this, List_usuarios.class);
                List_usuarios.statusForm("alterar");
                startActivity(i);
                break;
            case R.id.exclu_usuario:
                i = new Intent(this, List_usuarios.class);
                List_usuarios.statusForm("excluir");
                startActivity(i);
                break;
        }
    }
}