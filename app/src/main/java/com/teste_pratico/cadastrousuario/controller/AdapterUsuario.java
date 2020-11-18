package com.teste_pratico.cadastrousuario.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teste_pratico.cadastrousuario.R;
import com.teste_pratico.cadastrousuario.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.MyViewHolder>{
    private List<Usuario> listaUsuarios = new ArrayList<>();
    Config config = new Config();

    public AdapterUsuario(List<Usuario> lista) {
        listaUsuarios = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter_usuario, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        holder.nome.setText(usuario.getNome());
        holder.foto.setImageBitmap(config.converteString_Foto(usuario.getFoto()));
        holder.dataNascimento.setText("Data de Nascimento: " + usuario.getDataNascimento());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome;
        ImageView foto;
        TextView dataNascimento;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome  = itemView.findViewById(R.id.textViewNomeListAdapter);
            foto = itemView.findViewById(R.id.imageViewFotoListAdapter);
            dataNascimento = itemView.findViewById(R.id.textViewDataNascimentoListAdapter);
        }
    }
}
