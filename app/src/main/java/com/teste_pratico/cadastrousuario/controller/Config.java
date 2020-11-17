package com.teste_pratico.cadastrousuario.controller;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Config {
    //TEMPO DE EXECUÇÃO DA TELA SPLASH
    public static final int TIME_SPLASH = 2 * 1000;

    public String configDataApp(String data){
        String dataFormatada, DIA = "", MES = "", ANO = "";

        int dia, mes, ano;

        String[] dt = data.split("/");

        dia = Integer.parseInt(dt[0]);
        mes = Integer.parseInt(dt[1]);
        ano = Integer.parseInt(dt[2]);

        DIA = String.valueOf(dia);
        MES = String.valueOf(mes);
        ANO = String.valueOf(ano);

        if(dia < 10) {
            DIA = "0" + dt[0];
        }

        if(mes <10) {
            MES = "0" + dt[1];
        }

        dataFormatada = DIA + "/" + MES + "/" + ANO;

        return dataFormatada;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String configDataApi(String data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(data, formatter);

        String pronta = localDate.format(fmt);

        return pronta;
    }
}
