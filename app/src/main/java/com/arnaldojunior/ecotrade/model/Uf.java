package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uf uf1 = (Uf) o;
        return Objects.equals(nome, uf1.nome) &&
                Objects.equals(uf, uf1.uf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, uf);
    }

    @Override
    public String toString() {
        return "Uf{" +
                "nome='" + nome + '\'' +
                ", uf='" + uf + '\'' +
                '}';
    }
}