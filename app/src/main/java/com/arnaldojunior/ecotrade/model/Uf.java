package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Uf implements Serializable {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("uf")
    @Expose
    private String uf;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}