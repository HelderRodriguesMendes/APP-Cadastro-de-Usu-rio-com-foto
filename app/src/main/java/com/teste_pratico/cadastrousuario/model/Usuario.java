package com.teste_pratico.cadastrousuario.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDate;

public class Usuario implements Parcelable {

    private Long id;
    private String nome;
    private String dataNascimento;
    private String foto;
    private Boolean ativo;

    public Usuario() {}

    protected Usuario(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nome = in.readString();
        dataNascimento = in.readString();
        foto = in.readString();
        byte tmpAtivo = in.readByte();
        ativo = tmpAtivo == 0 ? null : tmpAtivo == 1;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getFoto() {
        return foto;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(nome);
        parcel.writeString(dataNascimento);
        parcel.writeString(foto);
        parcel.writeByte((byte) (ativo == null ? 0 : ativo ? 1 : 2));
    }
}
